package com.redditry

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.redditry.controller.User
import com.redditry.databinding.ActivityProfileEditBinding
import com.redditry.redditAPI.Pref
import com.redditry.redditAPI.SubredditEditData
import com.redditry.utils.RealPathUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.Locale
import java.util.SortedSet
import java.util.TreeSet
import kotlin.collections.HashMap

class EditProfileActivity : ActivityHead() {
    private lateinit var binding: ActivityProfileEditBinding
    private val user = User()
    private var type: String = ""

    // On create inflate the layout and set the edit button to finish the activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        binding.header.binding.editButton.setOnClickListener { finish() }

        navigationId = R.id.profil_icon
        navBar = binding.navBar

        val formEdit = binding.form.binding

        val countries: SortedSet<String> = TreeSet()
        for (locale in Locale.getAvailableLocales()) {
            if (!TextUtils.isEmpty(locale.displayCountry)) {
                countries.add(locale.displayCountry)
            }
        }

        val spinner = formEdit.countrySpinner
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            countries.toTypedArray()
        )
        spinner.adapter = adapter
        spinner.setSelection(adapter.getPosition(intent.getStringExtra("country")))

        formEdit.underageEdit.isChecked = intent.getBooleanExtra("underage", false)

        formEdit.buttonSave.setOnClickListener {
            val displayName = if (formEdit.displayNameEdit.editText != null) {
                formEdit.displayNameEdit.editText!!.text
            } else {
                intent.getStringExtra("username")
            }
            val description =
                if (formEdit.nameEdit.editText != null && formEdit.nameEdit.editText!!.text.isEmpty()) {
                    formEdit.nameEdit.editText?.text
                } else {
                    intent.getStringExtra("description")
                }
            val underage = formEdit.underageEdit.isChecked

            val countryName = formEdit.countrySpinner.selectedItem

            val countries: MutableMap<String, String> = HashMap()
            for (iso in Locale.getISOCountries()) {
                val l = Locale("", iso)
                countries[l.displayCountry] = iso
            }
            Thread {
                countries[countryName]?.let { it1 ->
                    user.setCountry(Pref(it1))
                }
                user.updateSubreddit(
                    SubredditEditData(
                        displayName.toString(),
                        description.toString(),
                        underage,
                        "any",
                        "user",
                        intent.getStringExtra("id")!!,
                        intent.getStringExtra("id")!!
                    )
                )
                val intent = Intent(this@EditProfileActivity, ProfileActivity::class.java)
                startActivity(intent)
            }.start()
        }

        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data

                val selectedImageUri = data?.data
                if (selectedImageUri != null) {
                    val realPath = RealPathUtil.getRealPath(this@EditProfileActivity, selectedImageUri)
                    val file = File(realPath)
                    val requestFile: RequestBody = RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        file
                    )

                    val imageBody = MultipartBody.Part.createFormData(
                        "file",
                        file.name,
                        requestFile
                    )

                    val parameters = mutableMapOf<String, String>()
                    if (type == "editButtonBanner") {
                        Thread {
                            val S3 = user.getS3Bucket(intent.getStringExtra("profilId")!!, file.name, "profileBanner", "image/" + file.extension)
                            S3?.s3UploadLease?.fields?.forEach {
                                parameters[it.name] = it.value
                            }
                            val imageUrl = user.uploadToS3("https:" + S3?.s3UploadLease?.action, parameters, imageBody)
                            user.changeProfileBanner(intent.getStringExtra("profilId")!!, imageUrl)
                            runOnUiThread {
                                Glide.with(this).load(Uri.parse(imageUrl)).into(binding.bannerAndPp.binding.banner)
                            }
                        }.start()
                    } else {
                        Thread {
                            val S3 = user.getS3Bucket(intent.getStringExtra("profilId")!!, file.name, "profileIcon", "image/" + file.extension)
                            S3?.s3UploadLease?.fields?.forEach {
                                parameters[it.name] = it.value
                            }
                            val imageUrl = user.uploadToS3("https:" + S3?.s3UploadLease?.action, parameters, imageBody)
                            user.changeProfileIcon(intent.getStringExtra("profilId")!!, imageUrl)
                            runOnUiThread {
                                Glide.with(this).load(Uri.parse(imageUrl)).into(binding.bannerAndPp.binding.picture)
                            }
                        }.start()
                    }
                }
            }
        }

        binding.bannerAndPp.binding.editButtonBanner.setOnClickListener {

            ActivityCompat.requestPermissions(this@EditProfileActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)

            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT

            type = "editButtonBanner"
            resultLauncher.launch(i)
        }

        binding.bannerAndPp.binding.editButtonPp.setOnClickListener {

            ActivityCompat.requestPermissions(this@EditProfileActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)

            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT

            type = "editButtonPp"
            resultLauncher.launch(i)
        }
    }
}
