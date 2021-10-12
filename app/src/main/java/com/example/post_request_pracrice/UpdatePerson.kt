package com.example.post_request_pracrice

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response

class UpdatePerson : AppCompatActivity() {
    lateinit var etName: EditText
    lateinit var etLocation: EditText
    lateinit var etNum: EditText
    lateinit var updateButton: Button
    lateinit var deleteButton: Button
    lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_person)

        etNum = findViewById(R.id.etNumber)
        etName = findViewById(R.id.etName)
        etLocation = findViewById(R.id.etLoc)
        updateButton = findViewById(R.id.buttonUpdate)
        cancelButton = findViewById(R.id.buttonCancel)
        deleteButton = findViewById(R.id.buttonDelete)

        updateButton.setOnClickListener{
            var id = etNum.text.toString().toInt()
            var person = Person.PersonDetails(etName.text.toString(),etLocation.text.toString(),id)
            Log.d("POST", "person is: $person")
            updatePerson(person,id)
            etNum.setText("")
            etName.setText("")
            etLocation.setText("")
        }
        deleteButton.setOnClickListener {
            var id = etNum.text.toString().toInt()
            deletePerson(id)
            Log.d("POST", "key is: ${id}")
            etNum.setText("")
            etName.setText("")
            etLocation.setText("")
        }
        cancelButton.setOnClickListener { cancelAddition() }

    }

    fun updatePerson(person: Person.PersonDetails, key:Int) {
        val progressDialog = ProgressDialog(this)
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
                progressDialog.setMessage("Please wait")
                progressDialog.show()
            }
            Log.d("POST", "fetch data")
            async {
                val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
                //val call: Call<Person.PersonDetails> = apiInterface!!.updatePerson(person.key!!,person)
                //val response: Response<Person.PersonDetails>
                try {
                    apiInterface!!.updatePerson(key,person).execute()
                    Log.d("POST", "fetch successful")
                }
                catch (e: Exception){ Log.d("POST", "ISSUE: $e") }
            }.await()
            withContext(Dispatchers.Main){
                progressDialog.dismiss() }
        }
    }

    fun deletePerson(key : Int) {
        val progressDialog = ProgressDialog(this)
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
                progressDialog.setMessage("Please wait")
                progressDialog.show()
            }
            Log.d("POST", "fetch data")
            async {
                val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
                val call = apiInterface!!.deletePerson(key)
                try {
                    val response = call.execute()
                    Log.d("POST", "fetch successful")
                }
                catch (e: Exception){ Log.d("POST", "ISSUE: $e") }
            }.await()
            withContext(Dispatchers.Main){
                progressDialog.dismiss() }
        }
    }

    fun cancelAddition() {
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}