use Glamazon;

create table ProductTypes (
    TID integer,
    type_name varchar(255),
    primary key (TID)
);

create table Brands (
    BID integer,
    name varchar(255),
    primary key (BID)
);

create table Products (
    UPC integer,
    product_name varchar(255),
    selling_cost decimal(6, 2),
    TID integer,
    BID integer,
    primary key (UPC),
    foreign key (TID) references ProductTypes(TID),
    foreign key (BID) references Brands(BID)
);

create table Vendors (
    VID integer,
    name varchar(255),
    address varchar(255),
    primary key (VID)
);

create table Supplies (
    VID integer,
    UPC integer,
    foreign key (VID) references Vendors(VID),
    foreign key (UPC) references Products(UPC)
);

create table Sells (
    VID integer,
    UPC integer,
    unit_cost decimal(7, 2),
    unit_size integer,
    shipping_time integer,
    foreign key (VID) references Vendors(VID),
    foreign key (UPC) references Products(UPC)
);

create table Regions (
    RID integer,
    primary key (RID)
);

create table Stores (
    SID integer,
    address varchar(255),
    RID integer,
    primary key (SID),
    foreign key (RID) references Regions(RID)
);

create table Customers (
    CID integer,
    last_name varchar(255),
    first_name varchar(255),
    address varchar(255),
    RID integer,
    primary key (CID),
    foreign key (RID) references Regions(RID)
);

create table Purchases (
    PID integer,
    date_time timestamp,
    CID integer,
    SID integer,
    primary key (PID),
    foreign key (CID) references Customers(CID),
    foreign key (SID) references Stores(SID)
);

create table ProductsPurchased (
    PID integer,
    UPC integer,
    foreign key (PID) references Purchases(PID),
    foreign key (UPC) references Products(UPC)
);
