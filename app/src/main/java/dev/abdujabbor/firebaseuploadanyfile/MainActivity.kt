package dev.abdujabbor.firebaseuploadanyfile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dev.abdujabbor.firebaseuploadanyfile.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    var curfile: Uri? = null
    var imageRef = Firebase.storage.reference
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.ivImage.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(it, 0)
            }
        }
        binding.btnUploadImage.setOnClickListener {
            uploadFireStoreage("myimage")
        }
    }

    private fun uploadFireStoreage(fileName:String)= CoroutineScope(Dispatchers.IO).launch{
     try {

         curfile?.let {
             imageRef.child("images/$fileName").putFile(it).await()

         }
         withContext(Dispatchers.Main){
             Toast.makeText(this@MainActivity, "Successfully upload", Toast.LENGTH_SHORT).show()
         }
     }catch (e:Exception){
         withContext(Dispatchers.Main){
             Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
         }
     }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            data?.data?.let {
                curfile = it
                binding.ivImage.setImageURI(it)

            }
        }
    }
}