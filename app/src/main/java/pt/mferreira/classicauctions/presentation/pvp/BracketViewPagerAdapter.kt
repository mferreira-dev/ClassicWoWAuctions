package pt.mferreira.classicauctions.presentation.pvp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import pt.mferreira.classicauctions.domain.entities.BracketsWrapper.Bracket
import pt.mferreira.classicauctions.presentation.pvp.teams.PvPTeamsFragment

class BracketViewPagerAdapter(
	private val brackets: List<Bracket>,
	private val dates: Pair<Long, Long>,
	manager: FragmentManager
) :
	FragmentStatePagerAdapter(
		manager,
		FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
	) {

	override fun getCount(): Int {
		return brackets.size
	}

	override fun getItem(position: Int): Fragment {
		return PvPTeamsFragment.newInstance(brackets[position], dates)
	}
}