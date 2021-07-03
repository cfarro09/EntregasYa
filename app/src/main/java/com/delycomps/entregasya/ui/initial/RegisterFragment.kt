package com.delycomps.entregasya.ui.initial

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.delycomps.entregasya.HomeActivity
import com.delycomps.entregasya.R
import com.delycomps.entregasya.cache.SharedPrefsCache
import com.delycomps.entregasya.webservice.Repository
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegisterFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val builderLoading: AlertDialog.Builder = AlertDialog.Builder(context)
        builderLoading.setCancelable(false) // if you want user to wait for some process to finish,
        builderLoading.setView(R.layout.layout_loading_dialog)
        val dialogLoading: AlertDialog = builderLoading.create()

        register_access.setOnClickListener {
            val firstName = register_name.text.toString()
            val lastName = register_last_name.text.toString()
            val email = register_email.text.toString()
            val username = register_username.text.toString()
            val password = register_password.text.toString()

            if (firstName != "" && lastName != "" && email != "" && username != "" && password != "") {
                dialogLoading.show()
                Repository().register(firstName, lastName, email, username, password) { isSuccess, result, message ->
                    dialogLoading.hide()
                    if (isSuccess) {
                        context?.let { it1 -> {
                            SharedPrefsCache(it1).set("fullname", result!!.fullName, "string")
                            SharedPrefsCache(it1).set("token", result.token, "string")
                            it1.startActivity(Intent(it1, HomeActivity::class.java))
                        }}
                    } else
                        Snackbar.make(view, message as CharSequence, Snackbar.LENGTH_LONG).setBackgroundTint(resources.getColor(
                            R.color.colorPrimary
                        )).show()
                }
            } else {
                Snackbar.make(view, resources.getString(R.string.all_obligatory) as CharSequence, Snackbar.LENGTH_LONG).setBackgroundTint(resources.getColor(
                    R.color.colorPrimary
                )).show()
            }
        }

        view.findViewById<TextView>(R.id.register_register).setOnClickListener {
            (requireActivity().findViewById<View>(R.id.tabs) as TabLayout).getTabAt(0)!!.select()
        }
    }
}
