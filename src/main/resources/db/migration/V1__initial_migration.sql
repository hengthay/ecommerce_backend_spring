create table users (
   id bigint auto_increment primary key,
   email varchar(255) UNIQUE not null,
   username varchar(255) not null,
   password varchar(255) not null,
   role varchar(50) default 'USER' not null,
   is_active boolean default true,
   created_at timestamp  default current_timestamp   not null,
   updated_at timestamp default current_timestamp on update current_timestamp not null
);

create table categories (
    id bigint auto_increment primary key,
    name varchar(255) UNIQUE not null,
    description text null,
    type varchar(50) not null,
    is_active boolean default true not null,
    created_at timestamp  default current_timestamp   not null,
    updated_at timestamp default current_timestamp on update current_timestamp not null
);

create table products (
    id bigint auto_increment primary key,
    title varchar(255) not null,
    price decimal(10, 2) not null,
    stock int default 0 not null,
    image_url text not null,
    description text null,
    is_active boolean default true not null,
    category_id bigint not null,
    created_at timestamp  default current_timestamp   not null,
    updated_at timestamp default current_timestamp on update current_timestamp not null,
    constraint chk_products_price check (price >= 0),
    constraint products_categories_id_fk
      foreign key (category_id) references categories (id)
);

create table carts (
    id binary(16)  default (uuid_to_bin(uuid(), 1)) primary key,
    user_id bigint not null,
    is_active boolean default true not null,
    created_at timestamp  default current_timestamp   not null,
    updated_at timestamp default current_timestamp on update current_timestamp not null,
    constraint carts_users_id_fk
       foreign key (user_id) references users (id) on delete cascade
);

create table cart_items (
    id binary(16)  default (uuid_to_bin(uuid(), 1)) primary key,
    cart_id binary(16) not null,
    product_id bigint not null,
    quantity int default 1 not null,
    created_at timestamp  default current_timestamp   not null,
    updated_at timestamp default current_timestamp on update current_timestamp not null,
    constraint uq_cart_product unique (cart_id, product_id),
    constraint chk_cart_items_qty check (quantity > 0),
    constraint cart_items_carts_id_fk
        foreign key (cart_id) references carts (id) on delete cascade,
    constraint cart_items_products_id_fk
        foreign key (product_id) references products (id)
);

create table addresses (
    id bigint auto_increment primary key,
    user_id bigint not null,
    street varchar(100) not null,
    city varchar(255) not null,
    country varchar(255) not null,
    postal_code varchar(20) not null,
    phone_number varchar(50) not null,
    is_active boolean default true not null,
    created_at timestamp  default current_timestamp   not null,
    constraint addresses_users_id_fk
       foreign key (user_id) references users (id)
);

create table orders (
    id binary(16)  default (uuid_to_bin(uuid(), 1)) primary key,
    user_id bigint not null,
    total_amount decimal(10, 2) not null,
    status varchar(20) default 'PENDING' not null,
    payment_status varchar(20) default 'UNPAID' not null,
    address_id bigint not null,
    created_at timestamp  default current_timestamp   not null,
    updated_at timestamp default current_timestamp on update current_timestamp not null,
    constraint orders_users_id_fk
        foreign key (user_id) references users (id),
    constraint orders_addresses_id_fk
        foreign key (address_id) references addresses (id)
);

create table order_items (
    id binary(16)  default (uuid_to_bin(uuid(), 1)) primary key,
    order_id binary(16) not null,
    product_id bigint not null,
    quantity int not null,
    price_at_purchase decimal(10, 2) not null,
    created_at timestamp  default current_timestamp   not null,
    updated_at timestamp default current_timestamp on update current_timestamp not null,
    constraint uq_order_product unique (order_id, product_id),
    constraint order_items_orders_id_fk
     foreign key (order_id) references orders (id),
    constraint orders_items_products_id_fk
     foreign key (product_id) references products (id)
);