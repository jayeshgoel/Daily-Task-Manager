package com.example.todolistapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.todolistapp.databinding.ActivityDetailsBinding
import com.example.todolistapp.model.TaskDatabase
import com.example.todolistapp.model.ToDoItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DetailsActivity : AppCompatActivity() {
    val binding:ActivityDetailsBinding by lazy{
        ActivityDetailsBinding.inflate(layoutInflater)
    }
    var calendar: Calendar?=null
    private lateinit var heading:String
    private lateinit var description:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val isCurrentlyPresent=intent.getBooleanExtra("isCurrentlyPresent",false)
        var item: ToDoItem? =null


        calendar?.let {
            Log.d("CALENDAR","AFTER "  + calendar!!.time)
        }
        if(isCurrentlyPresent) {
            item = intent.getSerializableExtra("currentToDo") as ToDoItem
            if (item != null) {
                binding.detailHeading.setText(item.heading)
                binding.detailDescription.setText(item.description)
                calendar=Calendar.getInstance()
                calendar?.time=item.date
                setDate()


            }
        }
        else{
            calendar=intent.getSerializableExtra("calendar") as Calendar?
            setDate()
        }






        binding.backBtn.setOnClickListener{
            finish()
        }

        binding.saveBtn.setOnClickListener{
            heading=binding.detailHeading.text.toString()
            description=binding.detailDescription.text.toString()
            if(heading.isEmpty()){
                Toast.makeText(this,"Add Heading",Toast.LENGTH_LONG).show()
            }
            else{
                saveData(heading,description,isCurrentlyPresent,item)
            }
        }

        binding.currentDate.setOnClickListener{
            showDatePickerDialog()
        }
    }

    fun setDate() {

        val date = calendar?.get(Calendar.DATE)
        val month = calendar?.get(Calendar.MONTH)   // Add 1 because months are 0-based
        val year = calendar?.get(Calendar.YEAR)
        val monthName= arrayOf("JAN","FEB","MAR","APR","MAY","JUNE","JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER")
        binding.currentDate.text = String.format("%02d %s %04d", date,
            month?.let { monthName.get(it) }, year)

    }


    private fun saveData(heading: String, description: String,isCurrentlyPresent:Boolean,toDoItem: ToDoItem?) {


        val dao= TaskDatabase.getDatabase(this).taskDAO()
        val repository=TaskRepository(dao)
        val mainViewModel= ViewModelProvider(this,ViewModelFactory(repository)).get(MainViewModel::class.java)
        if(toDoItem!=null){
//            updated
            toDoItem?.heading=heading
            toDoItem?.description=description
            toDoItem.date= calendar?.time!!
            mainViewModel.updateTask(toDoItem!!)
            Toast.makeText(this,"Task Updated",Toast.LENGTH_LONG).show()
        }
        else{
            mainViewModel.insertTask(ToDoItem(0,heading,description,false, calendar!!.time))
            Toast.makeText(this,"Task Added",Toast.LENGTH_LONG).show()
        }

        finish()
    }
    private fun showDatePickerDialog() {
        if(calendar!=null) {
            val year = calendar?.get(Calendar.YEAR)
            val month = calendar?.get(Calendar.MONTH)
            val day = calendar?.get(Calendar.DATE)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    calendar?.set(selectedYear, selectedMonth, selectedDay)
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val selectedDate = dateFormat.format(calendar?.time)
                    Log.d("CALENDAR", calendar?.time.toString())
                    binding.currentDate.text=selectedDate

                },
                year!!,
                month!!,
                day!!
            )
            datePickerDialog.show()
        }
    }
}