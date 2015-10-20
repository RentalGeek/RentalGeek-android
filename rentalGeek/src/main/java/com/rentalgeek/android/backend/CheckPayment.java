package com.rentalgeek.android.backend;

import java.util.List;

public class CheckPayment {

    public List<Transaction> transactions;

    public class Transaction {
        public String id;
        public String transaction_id;
        public String user_id;
        public String created_at;
        public String amount;
        public String card_type;
        public String cardholder_name;
        public String purchased_type;
    }

}
