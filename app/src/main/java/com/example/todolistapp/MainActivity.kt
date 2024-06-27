package com.example.todolistapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.END
import androidx.recyclerview.widget.ItemTouchHelper.START
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.adapter.ToDoAdapter
import com.example.todolistapp.databinding.ActivityMainBinding

import com.example.todolistapp.model.TaskDatabase
import com.example.todolistapp.model.ToDoItem
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity(),ToDoAdapter.onItemClickListener {

    
    private lateinit var calendar: Calendar

    lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ToDoAdapter

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)


        calendar=Calendar.getInstance()

        binding.goToPreviousDate.setText("<")
        binding.goToNextDate.setText(">")
        setDate()

        binding.textViewDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.goToPreviousDate.setOnClickListener{
                calendar.add(Calendar.DAY_OF_YEAR,-1)
                mainViewModel.setCurrentDate(calendar.time)
                setDate()
                setDataAndAdapter()
        }
        binding.goToNextDate.setOnClickListener{
                calendar.add(Calendar.DAY_OF_YEAR,+1)
                mainViewModel.setCurrentDate(calendar.time)
                setDate()
                setDataAndAdapter()
        }

        binding.floatingActionButton.setOnClickListener{
            val intent= Intent(this,DetailsActivity::class.java)
            val item=ToDoItem(0,"test","",false,Date())
            intent.putExtra("item",item)
            intent.putExtra("calendar",calendar)
            Log.d("CALENDAR","BEFORE " + calendar.time.toString() )
            startActivity(intent)
        }

        adapter = ToDoAdapter(this,this)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter



        val dao=TaskDatabase.getDatabase(this).taskDAO()
        val repository=TaskRepository(dao)
        mainViewModel=ViewModelProvider(this,ViewModelFactory(repository)).get(MainViewModel::class.java)

        mainViewModel.getTodayTask()?.observe(this, Observer { items ->

            if (items.isEmpty()) {
                binding.noTaskTxt.visibility = View.VISIBLE
                binding.progressBarLinearLayout.visibility = View.INVISIBLE
            } else {
                binding.progressBarLinearLayout.visibility = View.VISIBLE
                binding.noTaskTxt.visibility = View.INVISIBLE
            }

            Log.d("UpdatedChanges", "CURRENT FETCH START")
            items.forEach {
                Log.d("UpdatedChanges", it.heading)
            }
            Log.d("UpdatedChanges", "CURRENT FETCH END")
            adapter.updateList(items)
            setProgress()
        })






    }


    fun setProgress(){
        val val1:Int=(adapter.numberOfCheckBox)*100

        val val2:Int=adapter.totalItems
        if(val2!=0){
            binding.progressBar2.progress=val1/val2
            binding.progressPercentage.text=binding.progressBar2.progress.toString().plus("%")
        }


    }

    override fun onStart() {
        super.onStart()
        setDataAndAdapter()
    }

 



    private fun setDataAndAdapter() {
//        adapter = ToDoAdapter(this,this)
//        binding.recyclerview.layoutManager = LinearLayoutManager(this)
//        binding.recyclerview.adapter = adapter

        // Observe LiveData from ViewModel



    }


    private fun setDate() {

        val date = calendar.get(Calendar.DATE)
        val month = calendar.get(Calendar.MONTH)   // Add 1 because months are 0-based
        val year = calendar.get(Calendar.YEAR)
        val monthName= arrayOf("JAN","FEB","MAR","APR","MAY","JUNE","JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER")
        binding.textViewDate.text = String.format("%02d %s %04d", date, monthName.get(month), year)

    }


    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DATE)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)

                setDate()
                setDataAndAdapter()

            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    override fun deleteTask(toDoItem: ToDoItem) {
        mainViewModel.deleteTask(toDoItem)
    }

    override fun viewTask(toDoItem: ToDoItem) {
        val intent= Intent(this,DetailsActivity::class.java)
        intent.putExtra("currentToDo",toDoItem)
        intent.putExtra("isCurrentlyPresent",true)
        startActivity(intent)
    }

    override fun onCheckBoxClicked(toDoItem: ToDoItem) {
        mainViewModel.updateTask(toDoItem)
        setProgress()
    }
    private val itemTouchHelper by lazy{
        val simpleItemCallBack=object:ItemTouchHelper.SimpleCallback(UP or DOWN or START or END,0){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val adapter=binding.recyclerview.adapter as ToDoAdapter
                val from =viewHolder.adapterPosition
                val to=target.adapterPosition
                adapter.notifyItemMoved(from,to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }
        }
        ItemTouchHelper(simpleItemCallBack)
    }

}