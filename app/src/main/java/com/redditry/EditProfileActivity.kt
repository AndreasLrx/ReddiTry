package com.redditry

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import com.redditry.controller.User
import com.redditry.databinding.ActivityProfileEditBinding
import com.redditry.redditAPI.Pref
import com.redditry.redditAPI.SubredditEditData
import java.util.*


class EditProfileActivity : ActivityHead() {
    private lateinit var binding: ActivityProfileEditBinding
    private val user = User()
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
    }
}
