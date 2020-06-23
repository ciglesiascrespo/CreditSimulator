package c.iglesias.creditsimulator.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import c.iglesias.creditsimulator.R;
import c.iglesias.creditsimulator.data.CreditViewModel;
import c.iglesias.creditsimulator.db.entity.CreditEntity;
import c.iglesias.creditsimulator.db.entity.DetailCreditEntity;

import static c.iglesias.creditsimulator.common.Utils.getDetailFromPlazo;

public class NewCreditDialogFragment extends DialogFragment {
    private View view;
    private EditText edtName, edtEntidad, edtPlazo, edtValor, edtTasa;
    Button btnGuardar, btnCancelar;

    private CreditViewModel mViewModel;

    public static NewCreditDialogFragment newInstance() {
        return new NewCreditDialogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_credit_dialog_fragment, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(getActivity())
                .get(CreditViewModel.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View title = getActivity().getLayoutInflater().inflate(R.layout.title_dialog, null);
        builder.setCustomTitle(title);



        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.new_credit_dialog_fragment, null);

        edtPlazo = view.findViewById(R.id.idEdtPlazo);
        edtValor = view.findViewById(R.id.idEdtValor);
        edtTasa = view.findViewById(R.id.idEdtTasa);

        edtValor.addTextChangedListener(new CustomTextWatcher(edtValor));
        edtEntidad = view.findViewById(R.id.idEdtEntidad);
        edtName = view.findViewById(R.id.idEdtNameCretir);
        btnCancelar = view.findViewById(R.id.idBtnCancelar);
        btnGuardar = view.findViewById(R.id.idBtnGuardar);

        btnGuardar.setOnClickListener(V -> {
            String name = edtName.getText().toString();
            String entidad = edtEntidad.getText().toString();
            String valor = edtValor.getText().toString();
            String plazo = edtPlazo.getText().toString();
            String tasa = edtTasa.getText().toString();

            boolean validate = true;

            if (name.isEmpty()) {
                edtName.setError(getString(R.string.str_error_field));
                validate = false;
            }

            if (tasa.isEmpty()) {
                edtTasa.setError(getString(R.string.str_error_field));
                validate = false;
            }

            if (entidad.isEmpty()) {
                edtEntidad.setError(getString(R.string.str_error_field));
                validate = false;
            }

            if (valor.isEmpty()) {
                edtValor.setError(getString(R.string.str_error_field));
                validate = false;
            }

            if (plazo.isEmpty()) {
                edtPlazo.setError(getString(R.string.str_error_field));
                validate = false;
            }

            if (validate) {
                CreditEntity creditEntity = new CreditEntity(name, Integer.parseInt(valor.replace(",", "").replace(".","")), Integer.parseInt(plazo), entidad, Double.valueOf(tasa));
                List<DetailCreditEntity> listDetail = getDetailFromPlazo(creditEntity.getValue(), creditEntity.getTasa(), creditEntity.getDuration());
                mViewModel.insertCredit(creditEntity, listDetail);
                mViewModel.showSnackBar(getString(R.string.str_insert_credit_ok));
                this.dismiss();
            }
        });


        btnCancelar.setOnClickListener(v -> this.dismiss());
        builder.setView(view);

        return builder.create();

    }



}
