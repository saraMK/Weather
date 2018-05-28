package kayanteam.weather.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kayanteam.weather.R;
import kayanteam.weather.Retrofit.Apis;
import kayanteam.weather.Retrofit.CitisModel;
import kayanteam.weather.Adapters.SearchAdapter;
import kayanteam.weather.Utilies.Operations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mosta on 5/26/2018.
 */
public class SearchFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<CitisModel.Model> arrayList;
    private SearchAdapter searchAdapter;

    public SearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        // request to get cities of boundary  box of egypt
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Operations.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        Apis.GEtallCities apis = retrofit.create(Apis.GEtallCities.class);
        Call<CitisModel> modelCall = apis.getData(Operations.APPID);
        modelCall.enqueue(new Callback<CitisModel>() {
            @Override
            public void onResponse(Call<CitisModel> call, Response<CitisModel> response) {
                arrayList = new ArrayList<CitisModel.Model>();
                arrayList = response.body().getList();
                searchAdapter = new SearchAdapter(getContext(), response.body().getList());
                recyclerView.setAdapter(searchAdapter);

            }

            @Override
            public void onFailure(Call<CitisModel> call, Throwable t) {
                Log.d("response", t.toString());
            }
        });
        return view;
    }

    public void filter(String input) {
        if (searchAdapter != null) {
            Log.d("arrayList", arrayList.size() + "");
            searchAdapter.setArrayList(arrayList);
            searchAdapter.filter(input);
        }
    }
}

