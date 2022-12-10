package com.weather.android.ui.place

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.v4.os.IResultReceiver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.weather.android.R
import kotlinx.android.synthetic.main.fragment_place.*
import kotlin.math.log

class PlaceFragment : Fragment() {
    //val viewModel by lazy { ViewModelProvider.of(this).get(PlaceViewModel::class.java) }
    //用provider来创建model
    val viewModel by lazy { ViewModelProvider(this,
        ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(PlaceViewModel::class.java)}
    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place,container,false)
    }


 /*   override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().lifecycle.addObserver(object :LifecycleEventObserver{
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event.targetState==Lifecycle.State.CREATED){
                    val layoutManager=LinearLayoutManager(activity)
                    recyclerView.layoutManager=layoutManager
                    adapter= PlaceAdapter(this@PlaceFragment,viewModel.placeList)
                    recyclerView.adapter=adapter
                    searchPlaceEdit.addTextChangedListener{editable ->
                        val content =editable.toString()
                        if (content.isNotEmpty()){
                            viewModel.searchPlaces(content)
                        }
                        else{
                            recyclerView.visibility=View.GONE
                            bgImageView.visibility=View.GONE
                            viewModel.placeList.clear()
                            adapter.notifyDataSetChanged()
                        }
                    }
                    viewModel.placeLiveData.observe(this@PlaceFragment, Observer { result ->
                        val places=result.getOrNull()
                        if (places!=null){
                            recyclerView.visibility=View.VISIBLE
                            bgImageView.visibility=View.GONE
                            viewModel.placeList.clear()
                            viewModel.placeList.addAll(places)
                            adapter.notifyDataSetChanged()
                        }
                        else{
                            Toast.makeText(activity,"未能查询到任何地点",Toast.LENGTH_SHORT).show()
                            result.exceptionOrNull()?.printStackTrace()
                        }
                    })

                    requireActivity().lifecycle.removeObserver(this)
                }
            }
        })
    }
*/

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager=LinearLayoutManager(activity)
        recyclerView.layoutManager=layoutManager
        adapter= PlaceAdapter(this,viewModel.placeList)
        recyclerView.adapter=adapter
        searchPlaceEdit.addTextChangedListener{editable ->
            val content =editable.toString()
            if (content.isNotEmpty()){
                viewModel.searchPlaces(content)
            }
            else{
                recyclerView.visibility=View.GONE
                bgImageView.visibility=View.GONE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
       viewModel.placeLiveData.observe(this, Observer { result ->
           val places=result.getOrNull()
           if (places!=null){
               recyclerView.visibility=View.VISIBLE
               bgImageView.visibility=View.GONE
               viewModel.placeList.clear()
               viewModel.placeList.addAll(places)
               adapter.notifyDataSetChanged()
           }
           else{
               print(result.toString())
               Toast.makeText(activity,"未能查询到任何地点",Toast.LENGTH_LONG).show()
               result.exceptionOrNull()?.printStackTrace()
           }
       })
    }
}