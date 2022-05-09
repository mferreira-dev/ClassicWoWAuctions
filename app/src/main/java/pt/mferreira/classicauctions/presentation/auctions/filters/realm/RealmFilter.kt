package pt.mferreira.classicauctions.presentation.auctions.filters.realm

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.filter_realm.view.*
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.data.entities.Realm

class RealmFilter(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
	private val view: View
	val viewModel by lazy { ViewModelProvider(context as ViewModelStoreOwner).get(RealmViewModel::class.java) }

	private lateinit var realmRecyclerView: RecyclerView
	private lateinit var realmAdapter: RealmAdapter
	private lateinit var realms: List<Realm>

	init {
		val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
		view = inflater.inflate(R.layout.filter_realm, this, true)

		setupUI()
		setupButtons()
		setupObservers()
	}

	private fun setupUI() {
		realmRecyclerView = list_realm_filter
		realmRecyclerView.layoutManager = LinearLayoutManager(context)
	}

	private fun setupButtons() {
		btn_realm_filter_apply.setOnClickListener {
			viewModel.onApplyClick(realms[realmAdapter.selectedIndex])
		}

		btn_realm_filter_close.setOnClickListener {
			viewModel.onClickClose()
		}
	}

	private fun setupObservers() {
		viewModel.realms.observe(context as LifecycleOwner, {
			realms = it
		})

		viewModel.currentRealm.observe(context as LifecycleOwner, {
			recallRealm(it)
		})
	}

	private fun recallRealm(realm: String) {
		realmAdapter = RealmAdapter(context, realms)
		realmRecyclerView.adapter = realmAdapter

		if (realm.isNotEmpty()) {
			realms.forEachIndexed { idx, ele ->
				if (ele.name == realm) {
					realmRecyclerView.scrollToPosition(idx)
					realmAdapter.selectedIndex = idx
				}
			}
		}
	}
}