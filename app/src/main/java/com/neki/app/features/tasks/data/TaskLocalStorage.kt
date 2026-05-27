package com.neki.app.features.tasks.data

import android.content.Context
import com.neki.app.features.tasks.domain.Attachment
import com.neki.app.features.tasks.domain.AttachmentType
import com.neki.app.features.tasks.domain.Priority
import com.neki.app.features.tasks.domain.RepeatOption
import com.neki.app.features.tasks.domain.SubTask
import com.neki.app.features.tasks.domain.Task
import com.neki.app.features.tasks.domain.TaskGroup
import org.json.JSONArray
import org.json.JSONObject

class TaskLocalStorage(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun loadTasks(): List<Task> {
        val raw = preferences.getString(KEY_TASKS, null) ?: return emptyList()

        return runCatching {
            val array = JSONArray(raw)
            buildList {
                for (index in 0 until array.length()) {
                    add(array.getJSONObject(index).toTask())
                }
            }
        }.getOrDefault(emptyList())
    }

    fun saveTasks(tasks: List<Task>) {
        val array = JSONArray()
        tasks.forEach { task ->
            array.put(task.toJson())
        }

        preferences.edit()
            .putString(KEY_TASKS, array.toString())
            .apply()
    }

    fun loadGroups(): List<TaskGroup> {
        val raw = preferences.getString(KEY_GROUPS, null) ?: return emptyList()

        return runCatching {
            val array = JSONArray(raw)
            buildList {
                for (index in 0 until array.length()) {
                    add(array.getJSONObject(index).toTaskGroup())
                }
            }
        }.getOrDefault(emptyList())
    }

    fun saveGroups(groups: List<TaskGroup>) {
        val array = JSONArray()
        groups.forEach { group ->
            array.put(group.toJson())
        }

        preferences.edit()
            .putString(KEY_GROUPS, array.toString())
            .apply()
    }

    private fun Task.toJson(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("title", title)
            put("description", description)
            put("completed", completed)
            put("priority", priority.name)
            put("dueDate", dueDate)
            put("dueTime", dueTime)
            put("repeatOption", repeatOption.name)
            put("notificationsEnabled", notificationsEnabled)
            put("group", group?.toJson())
            put("subTasks", JSONArray().apply { subTasks.forEach { put(it.toJson()) } })
            put("attachments", JSONArray().apply { attachments.forEach { put(it.toJson()) } })
            put("createdAt", createdAt)
            put("updatedAt", updatedAt)
        }
    }

    private fun JSONObject.toTask(): Task {
        return Task(
            id = getString("id"),
            title = getString("title"),
            description = optString("description", ""),
            completed = optBoolean("completed", false),
            priority = enumValueOrDefault(optString("priority"), Priority.MEDIUM),
            dueDate = optNullableString("dueDate"),
            dueTime = optNullableString("dueTime"),
            repeatOption = enumValueOrDefault(optString("repeatOption"), RepeatOption.NONE),
            notificationsEnabled = optBoolean("notificationsEnabled", false),
            attachments = optJSONArray("attachments")?.toAttachments().orEmpty(),
            group = optJSONObject("group")?.toTaskGroup(),
            subTasks = optJSONArray("subTasks")?.toSubTasks().orEmpty(),
            createdAt = optLong("createdAt", System.currentTimeMillis()),
            updatedAt = optLong("updatedAt", System.currentTimeMillis())
        )
    }

    private fun TaskGroup.toJson(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("name", name)
        }
    }

    private fun JSONObject.toTaskGroup(): TaskGroup {
        return TaskGroup(
            id = getString("id"),
            name = getString("name")
        )
    }

    private fun SubTask.toJson(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("title", title)
            put("completed", completed)
        }
    }

    private fun JSONObject.toSubTask(): SubTask {
        return SubTask(
            id = getString("id"),
            title = getString("title"),
            completed = optBoolean("completed", false)
        )
    }

    private fun Attachment.toJson(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("type", type.name)
            put("uri", uri)
            put("name", name)
        }
    }

    private fun JSONObject.toAttachment(): Attachment {
        return Attachment(
            id = getString("id"),
            type = enumValueOrDefault(optString("type"), AttachmentType.FILE),
            uri = getString("uri"),
            name = optString("name", "Documento")
        )
    }

    private fun JSONArray.toSubTasks(): List<SubTask> {
        return buildList {
            for (index in 0 until length()) {
                add(getJSONObject(index).toSubTask())
            }
        }
    }

    private fun JSONArray.toAttachments(): List<Attachment> {
        return buildList {
            for (index in 0 until length()) {
                add(getJSONObject(index).toAttachment())
            }
        }
    }

    private fun JSONObject.optNullableString(key: String): String? {
        if (!has(key) || isNull(key)) return null
        return optString(key).takeIf { it.isNotBlank() }
    }

    private inline fun <reified T : Enum<T>> enumValueOrDefault(rawValue: String?, default: T): T {
        return runCatching {
            enumValueOf<T>(rawValue.orEmpty())
        }.getOrDefault(default)
    }

    private companion object {
        const val PREFS_NAME = "neki_tasks_storage"
        const val KEY_TASKS = "tasks"
        const val KEY_GROUPS = "groups"
    }
}
