package c.iglesias.creditsimulator.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import c.iglesias.creditsimulator.R;
import c.iglesias.creditsimulator.common.Constants;
import c.iglesias.creditsimulator.common.Utils;
import c.iglesias.creditsimulator.data.CreditViewModel;
import c.iglesias.creditsimulator.db.entity.CreditEntity;

import static c.iglesias.creditsimulator.common.Utils.getCuota;

public class CreditListAdapter extends RecyclerView.Adapter<CreditListAdapter.ViewHolder> {


    private List<CreditEntity> listItems;
    private Context context;
    private CreditViewModel viewModel;

    public CreditListAdapter(List<CreditEntity> list, Context context) {
        listItems = list;
        this.context = context;
        viewModel = new ViewModelProvider((AppCompatActivity) context)
                .get(CreditViewModel.class);

    }

    @NonNull
    @Override
    public CreditListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.credit_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditListAdapter.ViewHolder holder, int position) {
        holder.item = listItems.get(position);

        holder.txtNameBank.setText(holder.item.getNameBank());
        holder.txtName.setText(holder.item.getName());
        holder.txtPlazo.setText(String.valueOf(holder.item.getDuration()));
        holder.txtValor.setText(Utils.formatNumber(String.valueOf(holder.item.getValue()), false));
        holder.txtTasa.setText(String.valueOf(holder.item.getTasa()));

        holder.imgEdit.setOnClickListener(v -> {
            showDialogEditCredit(holder.item);
        });


        holder.mView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.ARG_ID_CREDIT, holder.item.getId());

            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(Constants.ARGS, bundle);
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void setListItems(List<CreditEntity> list) {
        this.listItems = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView txtName, txtValor, txtPlazo, txtNameBank, txtTasa;
        public CreditEntity item;
        public ImageView imgEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            txtName = mView.findViewById(R.id.idTxtCreditNameDetail);
            txtValor = mView.findViewById(R.id.idTxtValorDetail);
            txtPlazo = mView.findViewById(R.id.idTxtPlazoDetail);
            txtNameBank = mView.findViewById(R.id.idTxtEntidadDetail);
            txtTasa = mView.findViewById(R.id.idTxtTasaDetail);
            imgEdit = mView.findViewById(R.id.idImgEdit);

        }
    }

    private void showDialogEditCredit(CreditEntity entity) {
        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
        int cuota = (int) getCuota(entity.getValue(), entity.getTasa() / 100, entity.getDuration());
        EditCreditDialogFragment dialog = new EditCreditDialogFragment(entity, cuota);
        dialog.show(fm, "TAG");
    }
}
