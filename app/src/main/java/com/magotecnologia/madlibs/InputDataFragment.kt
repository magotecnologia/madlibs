package com.magotecnologia.madlibs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
    private val dataToFill = mutableListOf<String>()
    private val savedWords = mutableListOf<String>()

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
        binding.txNumberRemainingWords.text = dataToFill.size.toString()
        binding.etInputWord.hint = dataToFill.first()
        binding.txWordType.text = dataToFill.first()
        binding.btInputWord.setOnClickListener {
            saveWord()
        }
    }

    private fun saveWord() {
        if (binding.etInputWord.text.toString().isNotBlank()) {
            savedWords.add(binding.etInputWord.text.toString())
            if (savedWords.size < dataToFill.size) {
                Toast.makeText(
                    this.requireContext(),
                    resources.getString(R.string.input_continue),
                    Toast.LENGTH_LONG
                ).show()
                binding.txNumberRemainingWords.text = (dataToFill.size - savedWords.size).toString()
                binding.etInputWord.text.clear()
                binding.etInputWord.hint = dataToFill[savedWords.size]
                binding.txWordType.text = dataToFill[savedWords.size]

            } else {
                val action = InputDataFragmentDirections.actionInputDataFragmentToTellingFragment(
                    savedWords.toTypedArray(), args.selectedStory
                )
                this.findNavController().navigate(action)
            }
        }
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
                        dataToFill.add(
                            foundString.removePrefix("<").removeSuffix(">")
                        )
                    }
                }
        }
        scan.close()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
