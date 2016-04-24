

package com.roshan.popularmovies.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.roshan.popularmovies.PopularMoviesApplication;
import com.squareup.leakcanary.RefWatcher;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import dagger.ObjectGraph;

/**
 * Base class for all fragments.
 * Binds views, watches memory leaks and performs dependency injections
 *
 * @see ButterKnife
 * @see RefWatcher
 * @see ObjectGraph
 */
public abstract class BaseFragment extends Fragment {

    private ObjectGraph mObjectGraph;
    private Toast mToast;

    @CallSuper
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        buildObjectGraph(activity);
    }

    @CallSuper
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @CallSuper
    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        mObjectGraph = null;
        super.onDestroy();
        PopularMoviesApplication.get(getActivity()).getRefWatcher().watch(this);
    }

    private void buildObjectGraph(Activity activity) {
        Object[] modules = getModules().toArray();
        if (modules.length > 0) {
            mObjectGraph = PopularMoviesApplication.get(activity).buildScopedObjectGraph(modules);
            mObjectGraph.inject(this);
        }
    }

    protected void showToast(String message) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    protected void showToast(@StringRes int resId) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT);
        mToast.show();
    }

    protected List<Object> getModules() {
        return Collections.emptyList();
    }
}
