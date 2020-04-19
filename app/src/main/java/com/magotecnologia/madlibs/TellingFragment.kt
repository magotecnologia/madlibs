package com.magotecnologia.madlibs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.magotecnologia.madlibs.databinding.FragmentTellingBinding
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class TellingFragment : Fragment() {
    private var _binding: FragmentTellingBinding? = null
    private val binding get() = _binding!!
    private val args: TellingFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTellingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val story = createStory(args)
        binding.txMadLibContent.text = story

    }

    private fun createStory(args: TellingFragmentArgs): String {
        val rawNameWithoutFormat = args.story.rawName.removeSuffix(".txt")
        val location =
            resources.getIdentifier(rawNameWithoutFormat, "raw", this.requireContext().packageName)
        val scan = Scanner(resources.openRawResource(location))
        var actualWordToReplace = 0

        val strBuilder = StringBuilder();

        while (scan.hasNextLine()) {
            val tagStringRegex = "<[^>]+>"
            val regex = Regex(tagStringRegex)
            var foundLine = scan.nextLine()
            val line = regex.findAll(foundLine)
            val replaced = line.toList().flatMap { it.groups }.mapNotNull { it?.range }
            var actualBleed = 0
            for (replaceRange in replaced) {
                foundLine = foundLine.replaceRange(
                    replaceRange.first + actualBleed,
                    replaceRange.last + 1 + actualBleed,
                    args.words[actualWordToReplace]
                )
                actualWordToReplace++
                actualBleed + 2
            }
            strBuilder.appendln(foundLine)
        }
        scan.close()
        return strBuilder.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
