package com.example.simpletodo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Method to get the file
        fun getDataFile() : File {
            return File(filesDir, "data.txt")
        }

        // Method to load the items
        fun loadItems() {
            try {
                listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
        }

        // Save Items
        fun saveItems() {
            try {
                FileUtils.writeLines(getDataFile(), listOfTasks)
            } catch (ioException:IOException) {
                ioException.printStackTrace()
            }
        }

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // Remove Item from list
                listOfTasks.removeAt(position)
                // Notify adapters of change
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        // Load Items
        loadItems()

        // Sets up the Adapter to the recycle View
        val recycleView = findViewById<RecyclerView>(R.id.recycle)
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(this)

        val inputField = findViewById<EditText>(R.id.addTextField)

        // Set up button for input task
        findViewById<Button>(R.id.button).setOnClickListener{
            // Grab Text
            val input = inputField.text.toString()
            // Add to list of text
            listOfTasks.add(input)

            // Call adapter to update
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // Clear out text field
            inputField.setText("")

            //  Save items
            saveItems()
        }

    }
}