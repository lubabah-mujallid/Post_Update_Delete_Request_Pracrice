package com.example.post_request_pracrice

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var myList: ArrayList<Person.PersonDetails>
    lateinit var addButton: FloatingActionButton
    lateinit var refreshButton: FloatingActionButton
    lateinit var updateButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButton = floatingActionButton
        refreshButton = findViewById(R.id.refreshButton)
        updateButton = findViewById(R.id.UpdateButton)
        myList = ArrayList()

        val adapter = RecyclerAdapter(this, myList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        requestAPI()

        addButton.setOnClickListener { addPersonActivity() }
        refreshButton.setOnClickListener { requestAPI() }
        updateButton.setOnClickListener { updatePersonActivity() }
    }

    private fun requestAPI() {
        val progressDialog = ProgressDialog(this@MainActivity)
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
                progressDialog.setMessage("Please wait")
                progressDialog.show()
            }
            Log.d("MAIN", "fetch data")
            async { fetchData() }.await()
            if (myList.isNotEmpty()) {
                Log.d("MAIN", "Successfully got all data")
                withContext(Dispatchers.Main){
                    recyclerView.smoothScrollToPosition(myList.size-1)}
            } else {
                Log.d("MAIN", "Unable to get data")
                //Toast.makeText(this@MainActivity, "Couldn't Refresh Data, Please Try Again!", Toast.LENGTH_LONG).show()
            }
            withContext(Dispatchers.Main){
                progressDialog.dismiss() }
        }
    }

    private suspend fun fetchData() {
        Log.d("MAIN", "went inside fetch")
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        val call: Call<List<Person.PersonDetails>> = apiInterface!!.getPerson()
        val response: Response<List<Person.PersonDetails>>

        try {

            response = call.execute()
            withContext(Dispatchers.Main) {
                Log.d("MAIN", "fetch successful")
                for (Person in response.body()!!) {
                    updateTextView(Person)
                }
            }
        } catch (e: Exception) {
            Log.d("MAIN", "ISSUE: $e")
        }

    }

    private fun updateTextView(person: Person.PersonDetails) {
        myList.add(person)
        recyclerView.adapter?.notifyDataSetChanged()

    }

    private fun addPersonActivity() {
        Log.d("MAIN", "going to add activity")
        intent = Intent(applicationContext, PostPerson::class.java)
        startActivity(intent)
    }

    private fun updatePersonActivity(){
        Log.d("MAIN", "going to update activity")
        intent = Intent(applicationContext, UpdatePerson::class.java)
        startActivity(intent)
    }

}
/*
                myET.text.clear()
                myET.clearFocus()
                rvList.adapter?.notifyDataSetChanged()
* */


/*
get
    retrieve items from recycler
post
    add item to recycler

- 2 activities, 1 class, 3 layout files
---------------------------------------
- 1 activity: recycler view prints class
    - add retrofit and coroutines todo
- 2 activity: read user input to class (post) todo
    - connect with layout todo
---------------------------------------
- row_item layout for recycler
- 1 layout with recycler view and + button
- 2 layout with 2 buttons and 2 edit text todo
* */