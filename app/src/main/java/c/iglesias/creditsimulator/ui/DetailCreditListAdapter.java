package c.iglesias.creditsimulator.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import c.iglesias.creditsimulator.R;
import c.iglesias.creditsimulator.common.Utils;
import c.iglesias.creditsimulator.data.CreditViewModel;
import c.iglesias.creditsimulator.db.entity.DetailCreditEntity;

public class DetailCreditListAdapter extends RecyclerView.Adapter<DetailCreditListAdapter.ViewHolder> {


    private List<DetailCreditEntity> listItems;
    private Context context;
    private CreditViewModel viewModel;

    public DetailCreditListAdapter(List<DetailCreditEntity> list, Context context) {
        listItems = list;
        this.context = context;
        viewModel = new ViewModelProvider((AppCompatActivity) context)
                .get(CreditViewModel.class);

    }

    @NonNull
    @Override
    public DetailCreditListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_credit_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailCreditListAdapter.ViewHolder holder, int position) {
        holder.item = listItems.get(position);

        int saldo = position < getItemCount()-1 ? holder.item.getValorSaldo() : 0;

        holder.txtSaldo.setText(Utils.formatNumber(""+saldo,true));
        holder.txtACapital.setText(Utils.formatNumber("" + holder.item.getValorCapital(),true));
        holder.txtInteres.setText(Utils.formatNumber("" + holder.item.getValorInteres(),true));
        holder.txtNroCuota.setText(String.valueOf(holder.item.getNumCuota()));


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void setListItems(List<DetailCreditEntity> list) {
        this.listItems = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView txtNroCuota, txtSaldo, txtACapital, txtInteres;
        public DetailCreditEntity item;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            txtNroCuota = mView.findViewById(R.id.idTxtNroCuota);
            txtSaldo = mView.findViewById(R.id.idTxtSaldo);
            txtACapital = mView.findViewById(R.id.idTxtACapital);
            txtInteres = mView.findViewById(R.id.idTxtAInteres);

        }
    }
}
