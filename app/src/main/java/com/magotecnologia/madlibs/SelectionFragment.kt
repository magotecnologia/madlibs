package com.magotecnologia.madlibs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.magotecnologia.madlibs.databinding.FragmentSelectionBinding
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class SelectionFragment : Fragment() {
    private var _binding: FragmentSelectionBinding? = null
    private val binding get() = _binding!!
    private val stories = mutableListOf<Story>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillSpinner()
        binding.btStart.setOnClickListener {
            val selectedText = binding.spStorySelector.selectedItem as String
            val selectedStory = stories.first { story -> story.name == selectedText }
            val action =
                SelectionFragmentDirections.actionSelectionFragmentToInputDataFragment(selectedStory)
            it.findNavController().navigate(action)
        }
    }

    private fun fillSpinner() {
        val scan = Scanner(resources.openRawResource(R.raw.story_telling_list))

        while (scan.hasNextLine()) {
            val line = scan.nextLine().split(',')
            stories.add(Story(line[0], line[1]))
        }
        scan.close()
        val adapter = this.context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                stories.map { it.name })
        }
        binding.spStorySelector.adapter = adapter
    }
}


