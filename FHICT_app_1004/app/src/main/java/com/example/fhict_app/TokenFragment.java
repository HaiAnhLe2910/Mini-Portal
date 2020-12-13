package com.example.fhict_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import org.json.JSONException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TokenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TokenFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class TokenFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private static boolean authComplete = false;
    private static String authCode;

    /**
     *
     * @return fragment
     */
    public static TokenFragment newInstance() {
        TokenFragment fragment = new TokenFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public TokenFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FrameLayout ll = (FrameLayout) inflater.inflate(R.layout.fragment_token, container, false);

        WebView web = (WebView) ll.findViewById(R.id.webv_fr);
        web.getSettings().setJavaScriptEnabled(true);
        web.clearHistory();
        web.clearFormData();
        web.clearCache(true);
        android.webkit.CookieManager.getInstance().removeAllCookie();
        web.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                super.onPageStarted(view, url, favicon);
                if (authComplete){
                    try {
                        closeFragment();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.contains("access_token=") && !authComplete) {
                    authCode = url.split("#access_token=", 2)[1].split("&", 2)[0];
                    authComplete = true;
                    try {
                        closeFragment();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if(url.contains("error=access_denied")){
                    Log.i("", "ACCESS_DENIED_HERE");
                    authComplete = true;
                    try {
                        closeFragment();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        String CLIENT_ID = "i874073-no-production";
        String REDIRECT_URI="https://tas.fhict.nl/oob.html";
        String OAUTH_URL ="https://identity.fhict.nl/connect/authorize";
        String OAUTH_SCOPE="fhict fhict_personal";
        web.loadUrl(OAUTH_URL+"?redirect_uri="+REDIRECT_URI+"&response_type=token&client_id="+CLIENT_ID+"&scope="+OAUTH_SCOPE);

        return ll;
    }

    private void closeFragment() throws JSONException {
        View v = getView();
        if(v != null){
            v.setVisibility(View.GONE);
        }
        if(mListener != null){
            mListener.onFragmentInteraction(authCode);
        }
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PeopleFragment()).commit();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(String token) throws JSONException;
    }

}
