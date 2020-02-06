/*
 * MIT License
 *
 * Copyright (c) 2020 Web Factory LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package mk.webfactory.template.feature.home.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import mk.webfactory.template.R;
import mk.webfactory.template.feature.common.ui.BaseFragment;

public class HomeFragment extends BaseFragment implements HomeContract.View {

//Fixme: kotlin view binding  @BindView(R.id.circular_progress_view)
  View circularProgressView;

  @Inject
  HomeContract.Presenter presenter;
//  private Unbinder unbinder;

  @Inject public HomeFragment() {}

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home, container, false);
//    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onStart() {
    super.onStart();
    presenter.takeView(this);
  }

  @Override public void onStop() {
    super.onStop();
    presenter.dropView(this);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
//    unbinder.unbind();
  }

  @Override public void showLoadingIndicator(boolean active) {
    circularProgressView.setVisibility(active ? View.VISIBLE : View.GONE);
  }
}
