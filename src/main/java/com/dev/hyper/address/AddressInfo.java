package com.dev.hyper.address;

import com.dev.hyper.common.BaseEntity;
import com.dev.hyper.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "shipping_addresses")
@Entity
public class AddressInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(length = 255)
    private String addressDetail;

    @Column(nullable = false, length = 20)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public AddressInfo(String title, String address, String addressDetail, String code, User user) {
        this.title = title;
        this.address = address;
        this.addressDetail = addressDetail;
        this.code = code;
        this.user = user;

        if (user != null) {
            user.getAddresses().add(this);
        }
    }

    public void updateUser(User user) {
        this.user = user;
        user.getAddresses().add(this);
    }
}
