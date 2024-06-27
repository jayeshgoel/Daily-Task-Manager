package com.example.todolistapp.adapter

import android.content.Context
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.databinding.TodoitemBinding
import com.example.todolistapp.model.ToDoItem
import java.util.Calendar
import java.util.Date


class ToDoAdapter(private val context: Context,private val itemClickListener:onItemClickListener) :
    RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {


    interface onItemClickListener{

        fun deleteTask(toDoItem: ToDoItem)
        fun viewTask(toDoItem: ToDoItem)
        fun onCheckBoxClicked(toDoItem: ToDoItem)
    }

    var todoItems: MutableList<ToDoItem> = mutableListOf()
    var numberOfCheckBox=0;
    var totalItems=0;



    inner class ToDoViewHolder(private val binding:TodoitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {

                toDoItemView.setOnClickListener{
                    itemClickListener.viewTask(todoItems[position])
                }
                toDoText.text=todoItems[position].heading


                checkBox.isChecked=todoItems[position].isChecked
                checkBox.setOnClickListener{
                    todoItems[position].isChecked=!todoItems[position].isChecked
                    if(todoItems[position].isChecked){
                        numberOfCheckBox++
                    }
                    else{
                        numberOfCheckBox--
                    }
                    itemClickListener.onCheckBoxClicked(todoItems[position])
                }

                deleteToDoButton.setOnLongClickListener {
                    itemClickListener.deleteTask(todoItems[position])
                    Toast.makeText(context, "Task Deleted", Toast.LENGTH_SHORT).show()
                    deleteToDoButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                    true // Consumed the event
                }

                toDoTime.text= formattedDate(todoItems[position].date)



            }
        }
        fun formattedDate(date:Date):String{
            val calender=Calendar.getInstance()
            calender.time=date
            val hour=calender.time.hours
            val min=calender.time.minutes
            val ans=String.format("%02d : %02d",hour,min)
            return ans.toString()
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding=TodoitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return todoItems.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(position)
    }

    fun updateList(items : List<ToDoItem> ){
        todoItems.clear()
        todoItems.addAll(items)
        notifyDataSetChanged()
        numberOfCheckBox=0
        totalItems=todoItems.size
        for(item in todoItems){
            if(item.isChecked){
                numberOfCheckBox++
            }
        }
    }



}