package com.example.post_request_pracrice

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostPerson : AppCompatActivity() {
    lateinit var etName: EditText
    lateinit var etLocation: EditText
    lateinit var addButton: Button
    lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_person)
        etName = findViewById(R.id.etName)
        etLocation = findViewById(R.id.etLoc)
        addButton = findViewById(R.id.buttonSave)
        cancelButton = findViewById(R.id.buttonCancel)

        addButton.setOnClickListener {
            var person = Person.PersonDetails(etName.text.toString(),etLocation.text.toString())
            Log.d("POST", "person is: $person")
            newPerson(person)
            etName.setText("")
            etLocation.setText("")
        }
        cancelButton.setOnClickListener { cancelAddition() }
    }

    fun newPerson(person: Person.PersonDetails) {
        val progressDialog = ProgressDialog(this)
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
                progressDialog.setMessage("Please wait")
                progressDialog.show()
            }
            Log.d("POST", "fetch data")
            async {
                val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
                val call: Call<List<Person.PersonDetails>> = apiInterface!!.addPerson(person)
                val response: Response<List<Person.PersonDetails>>
                try {
                    response = call.execute()
                    Log.d("POST", "fetch successful")
                }
                catch (e: Exception){ Log.d("POST", "ISSUE: $e") }
            }.await()
            withContext(Dispatchers.Main){
                progressDialog.dismiss() }
        }
    }

    fun addPerson(person: Person.PersonDetails) {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

        if (apiInterface != null) {
            Log.d("MAIN", "Post interface not null")
            apiInterface.addPerson(person).enqueue(object : Callback<List<Person.PersonDetails>> {
                override fun onResponse(call: Call<List<Person.PersonDetails>>, response: Response<List<Person.PersonDetails>>) {
                    Log.d("MAIN", "adding success")
                    progressDialog.dismiss()
                }

                override fun onFailure(call: Call<List<Person.PersonDetails>>, t: Throwable) {
                    Log.d("MAIN", " adding failure" + "ISSUE: ")
                    progressDialog.dismiss()
                }
            })
        }
        else{ Log.d("MAIN", "Post interface null") }
    }


    fun cancelAddition() {
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}