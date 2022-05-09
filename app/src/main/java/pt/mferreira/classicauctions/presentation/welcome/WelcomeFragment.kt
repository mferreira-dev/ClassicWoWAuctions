package pt.mferreira.classicauctions.presentation.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import pt.mferreira.classicauctions.databinding.FragmentWelcomeBinding
import pt.mferreira.classicauctions.presentation.common.BaseFragment

class WelcomeFragment : BaseFragment() {
	private var _binding: FragmentWelcomeBinding? = null
	private val binding get() = _binding!!

	private lateinit var viewModel: WelcomeViewModel

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentWelcomeBinding.inflate(inflater, container, false)
		viewModel = ViewModelProvider(this).get(WelcomeViewModel::class.java)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		(requireActivity() as AppCompatActivity).supportActionBar?.hide()

		setupUI()
		setupButtons()
		setupObservers()
		setupStrings()
	}

	override fun setupUI() {

	}

	override fun setupButtons() {
		binding.btnNavigateToAuctions.setOnClickListener {
			val action =
				WelcomeFragmentDirections.actionWelcomeFragmentToAuctionsFragment()
			findNavController().navigate(action)
		}

		binding.btnNavigateToLeaderboards.setOnClickListener {
			val action =
				WelcomeFragmentDirections.actionWelcomeFragmentToPvpFragment()
			findNavController().navigate(action)
		}
	}

	override fun setupObservers() {

	}

	override fun setupStrings() {

	}

	override fun onResume() {
		super.onResume()
		activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}