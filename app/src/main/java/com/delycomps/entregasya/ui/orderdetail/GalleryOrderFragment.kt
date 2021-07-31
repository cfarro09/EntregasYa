package com.delycomps.entregasya.ui.orderdetail

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delycomps.entregasya.R
import com.delycomps.entregasya.adapters.AdapterGallery
import com.delycomps.entregasya.cache.SharedPrefsCache
import com.delycomps.entregasya.model.Order
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_new_order.*
import kotlinx.android.synthetic.main.fragment_gallery_order.view.*

class GalleryOrderFragment : Fragment() {

    private lateinit var manageViewModel: ManageViewModel
    private lateinit var dialogLoading: AlertDialog
    private lateinit var rvGallery: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        manageViewModel = ViewModelProviders.of(this).get(ManageViewModel::class.java)
        return inflater.inflate(R.layout.fragment_gallery_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val order: Order =  requireActivity().intent.getParcelableExtra("order")!!

        manageViewModel.getListImage(order.idOrder, SharedPrefsCache(requireContext()).getToken());

        val builderLoading: AlertDialog.Builder = AlertDialog.Builder(activity)
        builderLoading.setCancelable(false) // if you want user to wait for some process to finish,
        builderLoading.setView(R.layout.layout_loading_dialog)
        dialogLoading = builderLoading.create()

        val layoutManager = GridLayoutManager(requireContext(), 2)
        rvGallery = view.rv_from_gallery
        rvGallery.setHasFixedSize(true)
        rvGallery.layoutManager = layoutManager

        manageViewModel.listImages.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it.count() == 0) {
                    Toast.makeText(context, "Sin imágenes por mostrar.", Toast.LENGTH_LONG).show()
                }
                rvGallery.adapter = AdapterGallery(it.map{ x -> x.url})
            }
        })

        manageViewModel.loading.observe(viewLifecycleOwner, {
            if (it)
                dialogLoading.show()
            else
                dialogLoading.hide()
        })

        manageViewModel.textError.observe(viewLifecycleOwner, {
            Snackbar.make(view, it as CharSequence, Snackbar.LENGTH_LONG).setBackgroundTint(
                resources.getColor(
                    R.color.colorPrimary
                )
            ).show()
        })

    }
}
