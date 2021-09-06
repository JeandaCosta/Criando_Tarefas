package br.com.jc.criandotarefas.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.com.jc.criandotarefas.databinding.ActivityAddTaskBinding
import br.com.jc.criandotarefas.datasource.TaskDataSource
import br.com.jc.criandotarefas.extension.format
import br.com.jc.criandotarefas.extension.text
import br.com.jc.criandotarefas.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.tilTitle.text = it.title
                binding.tilDescription.text = it.description
                binding.tilDate.text = it.date
                binding.tilHour.text = it.hour
            }
        }

        insertListeners()
    }

    private fun insertListeners() {

        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()

            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tilDate.text = Date(it + offset).format()
            }

            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                binding.tilHour.text = "$hour:$minute"
            }

            timePicker.show(supportFragmentManager, null)
        }

        //Botão Criar Tarefa
        binding.btnCreateTask.setOnClickListener {
            val task = Task(
                id = intent.getIntExtra(TASK_ID, 0),
                title = binding.tilTitle.text,
                description = binding.tilDescription.text,
                date = binding.tilDate.text,
                hour = binding.tilHour.text
            )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()

        }

        //Botão Cancelar
        binding.btnCancel.setOnClickListener {
            finish()

        }

    }

    companion object {
        const val TASK_ID = "task"
    }

}