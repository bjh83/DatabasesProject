use Glamazon;

create table ProductTypes (
    TID integer not null auto_increment,
    type_name varchar(255) not null,
    primary key (TID)
);

create table Brands (
    BID integer not null auto_increment,
    name varchar(255) not null,
    primary key (BID)
);

create table Products (
    UPC integer not null,
    product_name varchar(255) not null,
    selling_cost decimal(6, 2) not null,
    TID integer,
    BID integer,
    primary key (UPC),
    foreign key (TID) references ProductTypes(TID),
    foreign key (BID) references Brands(BID)
);

create table Vendors (
    VID integer not null auto_increment,
    name varchar(255) not null,
    address varchar(255) not null,
    primary key (VID)
);

create table Supplies (
    VID integer not null,
    UPC integer not null,
    foreign key (VID) references Vendors(VID),
    foreign key (UPC) references Products(UPC)
);

create table Sells (
    VID integer not null,
    UPC integer not null,
    unit_cost decimal(7, 2) not null,
    unit_size integer not null,
    shipping_time integer not null,
    foreign key (VID) references Vendors(VID),
    foreign key (UPC) references Products(UPC)
);

create table Regions (
    RID integer not null auto_increment,
    primary key (RID)
);

create table Stores (
    SID integer not null auto_increment,
    address varchar(255) not null,
    RID integer not null,
    primary key (SID),
    foreign key (RID) references Regions(RID)
);

create table Stocks (
    SID integer not null,
    UPC integer not null,
    foreign key (SID) references Stores(SID),
    foreign key (UPC) references Products(UPC)
);

create table Customers (
    CID integer not null auto_increment,
    last_name varchar(255),
    first_name varchar(255),
    address varchar(255),
    RID integer,
    primary key (CID),
    foreign key (RID) references Regions(RID)
);

create table Purchases (
    PID integer not null auto_increment,
    date_time timestamp not null,
    CID integer not null,
    SID integer not null,
    primary key (PID),
    foreign key (CID) references Customers(CID),
    foreign key (SID) references Stores(SID)
);

create table ProductsPurchased (
    PID integer not null,
    UPC integer not null,
    foreign key (PID) references Purchases(PID),
    foreign key (UPC) references Products(UPC)
);
