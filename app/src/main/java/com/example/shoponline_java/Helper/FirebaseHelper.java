    package com.example.shoponline_java.Helper;

    import android.content.ClipData;
    import android.media.MediaPlayer;
    import android.util.Log;

    import androidx.annotation.NonNull;

    import com.example.shoponline_java.Model.Address;
    import com.example.shoponline_java.Model.Item;
    import com.example.shoponline_java.Model.Order;
    import com.example.shoponline_java.Model.User;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.List;

    import javax.security.auth.callback.Callback;

    public class FirebaseHelper {
        private DatabaseReference usersReference, cartsReference, addressReference, orderReference;
        public interface UserCallback {
            void onCallback(User user);
        }

        public interface UserIdCallback {
            void onCallback(String userId);
        }

        public interface CartCallBack{
            void onCallBack(List<Item> item);
        }

        public interface userIdCartCallBack{
            void onCallBack(String userId);
        }
        public interface AddressCallBack{
            void onCallBack(List<Address> item);
        }
        public interface OrderCompletedCallBack{
            void onCallBack(List<Order> item);
        }

        public interface OrderCallback {
            void onComplete(boolean isSuccess);
        }
        public static String getCurrentId(){
            return FirebaseAuth.getInstance().getUid();
        }

        public void getUserId(String phoneNumber, UserIdCallback callback) {
            usersReference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Duyệt qua tất cả các User có phoneNumber tương ứng
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String userId = dataSnapshot.getKey();
                            callback.onCallback(userId); // Gọi callback và truyền userId
                            return; // Dừng vòng lặp sau khi tìm thấy userId
                        }
                    }
                    // Nếu không tìm thấy userId tương ứng với phoneNumber
                    callback.onCallback(null);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi nếu có
                    callback.onCallback(null);
                }
            });
        }

        public static void getAllUser(){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        User user = dataSnapshot.getValue(User.class);
                        System.out.println(user);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    System.err.println("The read failed: " + error.getMessage());
                }
            });
        }

        public FirebaseHelper(){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            usersReference = database.getReference("Users");
            cartsReference = database.getReference("Carts");
            addressReference = database.getReference("Address");
            orderReference = database.getReference("Orders");
        }

        public void getCurrentIdInCart(userIdCartCallBack callback){
            cartsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String userId = dataSnapshot.getKey();
                        callback.onCallBack(userId);
                        break;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    callback.onCallBack(null);
                }
            });
        }

        public void addUsers(String userId, String phoneNumber, String password){
            User user = new User(userId, phoneNumber, password, null);
            usersReference.child(userId).setValue(user);
        }

        public void getPhoneNumber(String userId, UserCallback userCallback){
            DatabaseReference databaseReference = usersReference.child(userId);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        User user = snapshot.getValue(User.class);
                        if (user!=null){
                            userCallback.onCallback(user);
                        } else {
                            userCallback.onCallback(null);
                        }
                    } else {
                        userCallback.onCallback(null);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    userCallback.onCallback(null);
                }
            });
        }

        public void getPhoneNumberAndPassword(String phoneNumber, UserCallback userCallback){
            usersReference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = null;
                    if (snapshot.exists()){
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            user = dataSnapshot.getValue(User.class);
                            break;
                        }
                    }
                    userCallback.onCallback(user);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    userCallback.onCallback(null);
                }
            });
        }

        public void addCartItem(String userId, String productId, Item item){
            DatabaseReference databaseReference = cartsReference.child(userId).child(productId);
            databaseReference.setValue(item);
        }
        public void updateCartItemQuantity(String userId, String productId, int quantity){
            DatabaseReference databaseReference = cartsReference.child(userId).child(productId).child("numberInCart");
            databaseReference.setValue(quantity);
        }
        public void updateCartItemSize(String userId, String productId, String size, int quantity){
            DatabaseReference databaseReference = cartsReference.child(userId).child(productId).child("sizes").child(size);
            databaseReference.setValue(quantity);
        }
        public void removeCartItem(String userId, String productId){
            DatabaseReference databaseReference = cartsReference.child(userId).child(productId);
            databaseReference.removeValue();
        }

        public void getAllItemInCart(String userId, final CartCallBack callback){
            DatabaseReference databaseReference = cartsReference.child(userId);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Item> list = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Item item = dataSnapshot.getValue(Item.class);
                        list.add(item);
                    }
                    callback.onCallBack(list);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    callback.onCallBack(null);
                }
            });
        }

        public static boolean isLogin(){
            if (getCurrentId() != null){
                return true;
            }else {
                return false;
            }
        }

        public static void logOut(){
            FirebaseAuth.getInstance().signOut();
        }

        public void addAddress(String userId, String address) {
            DatabaseReference databaseReference = addressReference.child(userId).push(); // Use push() to generate a unique key for each address
            databaseReference.setValue(new Address(userId, address));
        }

        public void getAllAddress(String userId, final AddressCallBack addressCallBack){
            DatabaseReference databaseReference = addressReference.child(userId);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        List<Address> addressList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Address address = dataSnapshot.getValue(Address.class);
                            addressList.add(address);
                        }
                        addressCallBack.onCallBack(addressList);
                    } else {
                        addressCallBack.onCallBack(new ArrayList<>());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    addressCallBack.onCallBack(null);
                }
            });
        }

        public void addOrder(String userId, List<Item> cartItems, String paymentMethod, String address, String estimatedDelivery, double totalPrice, OrderCallback callback) {
            DatabaseReference databaseReference = orderReference.child(userId);
            String orderId = orderReference.child(userId).push().getKey();
            if (orderId != null) {
                Order order = new Order(orderId, userId, cartItems, paymentMethod, address, estimatedDelivery, totalPrice);
                databaseReference.child(orderId).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("FirebaseHelper", "Order added successfully.");
                            callback.onComplete(true);
                        } else {
                            Log.e("FirebaseHelper", "Failed to add order: " + task.getException().getMessage());
                            callback.onComplete(false);
                        }
                    }
                });
            } else {
                Log.e("FirebaseHelper", "Failed to generate unique order ID.");
                callback.onComplete(false);
            }
        }

        public void getAllOrder(String userId, final OrderCompletedCallBack orderCompletedCallBack){
            DatabaseReference databaseReference = orderReference.child(userId);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        List<Order> orderList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Order order = dataSnapshot.getValue(Order.class);
                            orderList.add(order);
                        }
                        orderCompletedCallBack.onCallBack(orderList);
                    } else {
                        orderCompletedCallBack.onCallBack(new ArrayList<>());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    orderCompletedCallBack.onCallBack(null);
                }
            });
        }

    }
