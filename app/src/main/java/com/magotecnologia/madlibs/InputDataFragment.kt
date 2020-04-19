package com.magotecnologia.madlibs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.magotecnologia.madlibs.databinding.FragmentInputDataBinding
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class InputDataFragment : Fragment() {
    private var _binding: FragmentInputDataBinding? = null
    private val binding get() = _binding!!
    private val args: InputDataFragmentArgs by navArgs()
    private val words = emptyMap<String, String>()
    private val temporal = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInputDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rawName = args.selectedStory.rawName
        getAllFieldsToFill(rawName)
        temporal.forEach { binding.text.append(it) }
    }

    private fun getAllFieldsToFill(rawName: String) {
        val rawNameWithoutFormat = rawName.removeSuffix(".txt")
        val location =
            resources.getIdentifier(rawNameWithoutFormat, "raw", this.requireContext().packageName)
        val scan = Scanner(resources.openRawResource(location))
        while (scan.hasNextLine()) {
            val tagStringRegex = "<[^>]+>"
            val regex = Regex(tagStringRegex)
            val line = regex.findAll(scan.nextLine())
            line.toList()
                .flatMap { it.groups }
                .map { it?.value }
                .forEach {
                    it?.let { foundString ->
                        temporal.add(
                            foundString.removePrefix("<").removeSuffix(">")
                        )
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
