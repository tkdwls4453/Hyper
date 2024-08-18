package com.dev.hyper.item.repository;

import com.dev.hyper.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository {

}
