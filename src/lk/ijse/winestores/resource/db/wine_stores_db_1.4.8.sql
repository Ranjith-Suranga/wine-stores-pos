/*
Database script for a wine_stores. Version 1.4.8 Database cascades are not added to the script. 
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Special Note:
Developers are not allowed to modify following script under any circumstances.
if you need to get edit please contact Mr.Prasad at prasad@ijse.lk or 0772252985
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

Notes:

2016-08-29 :    id_pool table added to the database (Sura-Boy)   
2016-08-31 :  
                main_category.category_name -> Unique
                sub_category.sub_category_name -> Unique
                main_sub_item table added to the database
                item.item_name -> Unique
2016-09-01 :    
                barcode and capacity moved to main_sub_item from item table
2016-09-04 :
                supplier.name -> Unique
                grn_detail table's id name changed to grn_detail_id;
2016-09-05 :
                added new table called empty_bottle
2016-09-11 :
                added new table called item_details
*/

drop database wine_stores; 
create database wine_stores;
use wine_stores;

create table main_category(
	main_cat_id varchar(100) not null,
	category_name varchar(300) not null,
	constraint primary key(main_cat_id)
);
create table sub_category(
	sub_category_id varchar(100) not null,
	sub_category_name varchar(300) not null,
	constraint primary key(sub_category_id)
);
create table main_sub_category(
	main_sub_category_id varchar(100)not null,
	main_cat_id varchar(100) not null,
	sub_category_id varchar(100) not null,
	constraint primary key(main_sub_category_id),
	constraint foreign key(main_cat_id) references main_category(main_cat_id),
	constraint foreign key(sub_category_id) references sub_category(sub_category_id)	
);
create table item(
	item_id varchar(100) not null,
	item_name varchar(300) not null,
	constraint primary key(item_id)
);

CREATE TABLE `wine_stores`.`main_sub_item` (
    `main_sub_item_id` VARCHAR(100) NOT NULL,
    `main_sub_id` VARCHAR(100) NOT NULL,
    `item_id` VARCHAR(100) NOT NULL,
    item_code VARCHAR(100) NOT NULL,
    capacity varchar(300),
    barcode varchar(300),
    constraint PRIMARY KEY (`main_sub_item_id`),
    constraint foreign key (main_sub_id) REFERENCES main_sub_category(main_sub_category_id),
    constraint foreign key (item_id) REFERENCES item (item_id)
);


create table supplier(
	supplier_id varchar(100) not null,
	name varchar(300) not null,
	address varchar(300) not null,
	email varchar(300),
	contact varchar(200),
	fax varchar(200),
	cordintator_name varchar(300),
	cordinator_contact varchar(200),
	agent_name varchar(300),	
	agen_no varchar(200),
	supplier_added date,
	constraint primary key(supplier_id)
);
create table grn(
	grn_id varchar(100) not null,
	supplier_id varchar(100) not null,
        invoice_id varchar(100) not null,
	invoice_date date not null,
	grn_date date not null,
	who_made varchar(300) not null,
	edit_date_time datetime,
	constraint primary key(grn_id),
	constraint foreign key(supplier_id) references supplier(supplier_id)
);

create table batch(
	batch_id varchar(100) not null,
	grn_id varchar(100) not null,
	constraint primary key (batch_id),
        CONSTRAINT FOREIGN KEY (grn_id) REFERENCES grn(grn_id)
);

create table grn_detail(
	grn_detail_id varchar(100) not null,
	main_sub_item_id varchar(100) not null,
	batch_id varchar(100) not null,
	batch_qty int not null,
	selling_price decimal(10,2) not null,
	buying_price decimal(10,2) not null,
	expiry_date date,
	constraint primary key(grn_detail_id),
	constraint foreign key(batch_id) references batch(batch_id),
	constraint foreign key(main_sub_item_id) references main_sub_item(main_sub_item_id)
); 

create table item_batch_grn_detail(
	item_batch_grn_detail_id varchar(100) not null,
	main_sub_item_id varchar(100) not null,
	batch_id varchar(100) not null,
	batch_qty int not null,
	selling_price decimal(10,2) not null,
	buying_price decimal(10,2) not null,
	expiry_date date,
	start_qty int not null,
	constraint primary key(item_batch_grn_detail_id),
	constraint foreign key(batch_id) references batch(batch_id),
	constraint foreign key(main_sub_item_id) references main_sub_item(main_sub_item_id)
);

-- create table customer(
-- 	customer_id varchar(100) not null,
-- 	name varchar(300) not null,
-- 	email varchar(300) not null,
-- 	contact varchar(300),
-- 	constraint primary key(customer_id)
-- );


/* 
=========================================================================
    2016-08-29
    Added by Sura-Boy 
========================================================================
*/

CREATE TABLE `id_pool` (
    `old_id` varchar(25) NOT NULL,
    `table_name` varchar(25) NOT NULL,
    PRIMARY KEY (`old_id`,`table_name`)
);

/* 
=========================================================================
    2016-08-31
    Added by Sura-Boy 
========================================================================
*/

ALTER TABLE `wine_stores`.`main_category` 
ADD UNIQUE INDEX `category_name_UNIQUE` (`category_name` ASC);

ALTER TABLE `wine_stores`.`sub_category` 
ADD UNIQUE INDEX `sub_category_name_UNIQUE` (`sub_category_name` ASC);

ALTER TABLE `wine_stores`.`item` 
ADD UNIQUE INDEX `item_name_UNIQUE` (`item_name` ASC);

/* 
=========================================================================
    2016-09-04
    Added by Sura-Boy 
========================================================================
*/

ALTER TABLE `wine_stores`.`supplier` 
ADD UNIQUE INDEX `name_UNIQUE` (`name` ASC);

/* 
=========================================================================
    2016-09-05
    Added by Sura-Boy 
========================================================================
*/

CREATE TABLE `wine_stores`.`empty_bottle` (
  `bottle_type` VARCHAR(100) NOT NULL,
  `cost` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`bottle_type`),
  UNIQUE INDEX `bottle_type_UNIQUE` (`bottle_type` ASC));

CREATE TABLE grn_empty_bottle_detail (
    id VARCHAR(100) NOT NULL,
    estimated_bottles INT NOT NULL,
    given_bottles INT NOT NULL,
    due_date DATE NOT NULL,
    grn_id VARCHAR(100) NOT NULL,
    bottle_type VARCHAR(100) NOT NULL,
    CONSTRAINT FOREIGN KEY (grn_id) REFERENCES grn(grn_id),
    CONSTRAINT FOREIGN KEY (bottle_type) REFERENCES empty_bottle(bottle_type)
);

CREATE TABLE item_details(
    item_code VARCHAR(100) NOT NULL,
    qty INT NOT NULL,
    selling_price DECIMAL(10,2) NOT NULL,
    buying_price DECIMAL(10,2) NOT NULL,
    CONSTRAINT PRIMARY KEY (item_code)
);

create table payment(
        payment_id varchar(100) not null,
        payment_method varchar(100) not null,
        constraint primary key(payment_id)
);

create table custom_order(
	order_id int NOT NULL AUTO_INCREMENT,
	order_date DATETIME NOT NULL,
        made_by varchar(100) NOT NULL,
        total decimal(10,2) NOT NULL,
        payment_id varchar(100) not null,
	constraint primary key(order_id),
        constraint foreign key(payment_id) references payment(payment_id)
);

create table cheque_details(
        cheque_details_id int not null AUTO_INCREMENT,
        order_id int not null,
        cheque_number varchar(100) not null,
        bank varchar(100) not null,
        branch varchar(100) not null,
        constraint primary key(cheque_details_id),
        constraint foreign key(order_id) references custom_order(order_id)
);

create table order_item_details(
        order_item_details_id int not null AUTO_INCREMENT,
        order_id int not null,
        item_code varchar(100) not null,
        qty int not null,
        selling_price decimal(10,2) not null,
        constraint primary key(order_item_details_id),
        constraint foreign key(order_id) references custom_order(order_id),
        constraint foreign key(item_code) references item_details(item_code)
);

create table order_empty_bottle_details(
        order_empty_bottle_details_id int not null AUTO_INCREMENT,
        order_id int not null,
        bottle_type varchar(100) not null,
        qty int not null,
        total decimal(10,2) not null,
        constraint primary key(order_empty_bottle_details_id),
        constraint foreign key(order_id) references custom_order(order_id),
        constraint foreign key(bottle_type) references empty_bottle(bottle_type)        
);

create table customer(
        customer_id int not null AUTO_INCREMENT,
        customer_name varchar(350) not null,
        telephone_number varchar(350) not null,
        address varchar(450) not null,
        constraint primary key(customer_id)
);

create table credit_order(
	order_id int NOT NULL AUTO_INCREMENT,
	order_date DATETIME NOT NULL,
        made_by varchar(100) NOT NULL,
        total decimal(10,2) NOT NULL,
        customer_id int not null,
	constraint primary key(order_id),
        constraint foreign key(customer_id) references customer(customer_id)
);

create table credit_order_item_details(
        order_item_details_id int not null AUTO_INCREMENT,
        order_id int not null,
        item_code varchar(100) not null,
        qty int not null,
        selling_price decimal(10,2) not null,
        constraint primary key(order_item_details_id),
        constraint foreign key(order_id) references credit_order(order_id),
        constraint foreign key(item_code) references item_details(item_code)
);

create table credit_order_empty_bottle_details(
        order_empty_bottle_details_id int not null AUTO_INCREMENT,
        order_id int not null,
        bottle_type varchar(100) not null,
        qty int not null,
        total decimal(10,2) not null,
        constraint primary key(order_empty_bottle_details_id),
        constraint foreign key(order_id) references credit_order(order_id),
        constraint foreign key(bottle_type) references empty_bottle(bottle_type)        
);

create table supplier_order(
	order_id int not null AUTO_INCREMENT,
	supplier_id varchar(100) null,
	order_date date not null,
        made_by varchar(100) not null,
        order_total varchar(100) not null,
	constraint primary key(order_id),
	constraint foreign key(supplier_id) references supplier(supplier_id)
);

create table supplier_order_detail(
	order_detail_id int not null AUTO_INCREMENT,
	order_id int not null,
	item_code varchar(100) not null,
	qty int not null,
	buying_price decimal(10,2),
	constraint primary key(order_detail_id),
	constraint foreign key(order_id) references supplier_order(order_id)
);

-- OLD DATA
-- *********************************************************************************************************
-- INSERT INTO `empty_bottle` VALUES ('BOTTLE-1','200'),('BOTTLE-2','200');
-- INSERT INTO `item` VALUES ('0008','ABPL Anchor Strong 325ML'),('0007','ABPL Anchor Strong 625ml Bottle'),('0005','APBL Tiger Black 410 ml Bottle'),('0006','APBL Tiger Black 500ml Can'),('0004','APBL Tiger Black 640ml Bottle'),('0002','APBL Tiger Original 410ml Bottle'),('0003','APBL Tiger Original 500ml Can'),('0001','APBL Tiger Original 640ml Bottle');
-- INSERT INTO `main_category` VALUES ('05','APBL'),('01','DCSL'),('04','IDL'),('03','LION'),('02','PERICEYL');
-- 
-- INSERT INTO `sub_category` VALUES ('07','ABC Stout'),('06','Anchor Smooth'),('03','Anchor Strong'),('04','Baron Super Strong'),('05','Barons Strong'),('11','Blue Label'),('13','Coconut'),('16','D.D.A'),('10','Economy Special'),('08','Extra Special'),('18','Narikela'),('14','Old'),('19','Pack 3 in 1'),('09','Special'),('17','Sri Lanka'),('02','Tiger Black'),('01','Tiger Original'),('15','V.S.O.A'),('12','White Label');
-- 
-- INSERT INTO `main_sub_category` VALUES ('01','05','01'),('02','05','02'),('03','05','03'),('04','05','04'),('05','05','05'),('06','05','06'),('07','05','07'),('08','01','08'),('09','01','09'),('10','01','10'),('11','01','11'),('12','01','12'),('13','01','13'),('14','01','14'),('15','01','15'),('16','01','16'),('17','01','17'),('18','01','18'),('19','01','19');
-- INSERT INTO `main_sub_item` VALUES ('00000001','01','0001','050101','',''),('00000002','01','0002','050102','',''),('00000003','01','0003','050103','',''),('00000004','02','0004','050204','',''),('00000005','02','0005','050205','',''),('00000007','02','0006','050206','',''),('00000008','03','0007','050307','',''),('00000009','03','0008','050308','','');
-- INSERT INTO `supplier` VALUES ('001','DISTILLERIES COMPANY OF SRI LANKA PLC (DCSL)','110, Norris Canal Rd, Colombo - 10.',NULL,'0115 507000','-','','-','','',NULL),('002','PERICEYL (PVT) LIMITED (PERICEYL)','2 1/1A, High level Road, Colombo - 06',NULL,'0112 515146','-','','-','','',NULL),('003','LION BREWERY (CEYLON) PLC (LION)','254, Colombo Road, Biyagama.',NULL,'0112465900','-','','-','','',NULL),('004','INTERNATIONAL DISTILLERS LIMITED ( IDL)','Melford Estate, Kotalawela,\nKaduwela.',NULL,'0114653400','-','','-','','',NULL),('005','ASIA PACIFIC BREWERY (LANKA) LIMITED - (APBL)','548/B, Nawala Road, Rajagiriya.',NULL,'0114866575','011-2864918','','-','','',NULL);

-- INSERT INTO `empty_bottle` VALUES ('BOTTLE-1','200'),('BOTTLE-2','200');
-- 
-- INSERT INTO `main_category` VALUES ('05','APBL'),('01','DCSL'),('04','IDL'),('03','LION'),('02','PERICEYL');
-- 
-- INSERT INTO `sub_category` VALUES ('32','3 Coins German Beer'),('07','ABC Stout'),('06','Anchor Smooth'),('03','Anchor Strong'),('22','Arrack'),('04','Baron Super Strong'),('05','Barons Strong'),('21','Blended Arrack'),('11','Blue Label'),('34','Brandy'),('28','Calsberg'),('29','Calsberg Special Brew'),('13','Coconut'),('16','D.D.A'),('10','Economy Special'),('08','Extra Special'),('35','Gin'),('33','Lavina Dark - 30L'),('25','Lion Lager'),('26','Lion Stout'),('23','LMFI'),('18','Narikela'),('14','Old'),('19','Pack 3 in 1'),('20','Pure Coconut Arrack'),('36','Rum'),('30','Sando Power'),('31','Sando Stout'),('09','Special'),('17','Sri Lanka'),('27','Strong Beer'),('02','Tiger Black'),('01','Tiger Original'),('15','V.S.O.A'),('37','Vodka'),('24','Whisky'),('12','White Label');
-- 
-- INSERT INTO `item` VALUES ('0013','ABPL Anchor Smooth 325ml Bottle'),('0014','ABPL Anchor Smooth 330ml Can'),('0015','ABPL Anchor Smooth 500ml Can'),('0012','ABPL Anchor Smooth 625ml Bottle'),('0008','ABPL Anchor Strong 325ML Bottle'),('0016','ABPL Anchor Strong 330ml Can'),('0017','ABPL Anchor Strong 500ml Can'),('0007','ABPL Anchor Strong 625ml Bottle'),('0019','ABPL Baron Super Strong 330ml Can'),('0018','ABPL Baron Super Strong 625ml Bottle'),('0020','ABPL Baron uper Strong 500ml Can'),('0022','ABPL Barons Strong 330ml Can'),('0023','ABPL Barons Strong 500ml Can'),('0021','ABPL Barons Strong 625ml Bottle'),('0010','APBL ABC Stout 330ml Can'),('0011','APBL ABC Stout 500ml Can'),('0009','APBL ABC Stout 625ml Bottle'),('0005','APBL Tiger Black 410ml Bottle'),('0006','APBL Tiger Black 500ml Can'),('0004','APBL Tiger Black 640ml Bottle'),('0002','APBL Tiger Original 410ml Bottle'),('0003','APBL Tiger Original 500ml Can'),('0001','APBL Tiger Original 640ml Bottle'),('0148','Balmora Dark Rum 750ml'),('0149','Balmora White Rum 750ml'),('0032','DCSL Blue Label Arrack 180ml'),('0031','DCSL Blue Label Arrack 375ml'),('0030','DCSL Blue Label Arrack 750ml'),('0036','DCSL Coconut Arrack 180ml'),('0037','DCSL Coconut Arrack 375ml'),('0038','DCSL Coconut Arrack 750ml'),('0045','DCSL DDA Arrack 375ml'),('0044','DCSL DDA Arrack 750ml'),('0029','DCSL Economy Special Arrack 180ml'),('0026','DCSL Extra Special Arrack 180ml'),('0025','DCSL Extra Special Arrack 375ml'),('0024','DCSL Extra Special Arrack 750ml'),('0047','DCSL Narikela 750ml'),('0040','DCSL Old Arrack 180ml'),('0041','DCSL Old Arrack 375ml'),('0039','DCSL Old Arrack 750ml'),('0028','DCSL Special Arrack 175ml'),('0027','DCSL Special Arrack 750ml'),('0046','DCSL Sri Lanka Arrack 750ml'),('0043','DCSL VSOA Premium Arrack 375ml'),('0042','DCSL VSOA Premium Arrack 750ml'),('0048','DCSL VSOA/ DDA/ OA 375ml - 3 in1 Pack'),('0035','DCSL White Label Arrack 180ml'),('0034','DCSL White Label Arrack 375ml'),('0033','DCSL White Label Arrack 750ml'),('0146','Flinton Lemon Gin 180ml'),('0145','Flinton Lemon Gin 375ml'),('0144','Flinton Lemon Gin 750ml'),('0143','Flinton London Dry Gin 180ml'),('0142','Flinton London Dry Gin 375ml'),('0141','Flinton London Dry Gin 750ml'),('0133','House of Tilbury Whisky 750ml'),('0109','IDL Admiral White Rum 180ml'),('0108','IDL Admiral White Rum 375ml'),('0107','IDL Admiral White Rum 750ml'),('0083','IDL Ascot Dry Gin 180ml'),('0082','IDL Ascot Dry Gin 375ml'),('0081','IDL Ascot Dry Gin 750ml'),('0086','IDL Ascot Lemon Gin 180ml'),('0085','IDL Ascot Lemon Gin 375ml'),('0084','IDL Ascot Lemon Gin 750ml'),('0089','IDL Ascot Orange Gin 180ml'),('0088','IDL Ascot Orange Gin 375ml'),('0087','IDL Ascot Orange Gin 750ml'),('0056','IDL Blacl Label Arrack 180ml'),('0057','IDL Blacl Label Arrack 375ml'),('0058','IDL Blacl Label Arrack 750ml'),('0064','IDL Blue Sapphire 180ml'),('0063','IDL Blue Sapphire 375ml'),('0065','IDL Blue Sapphire 750ml'),('0062','IDL Blue Sapphire with Carton 750ml'),('0106','IDL Calypso Gold Red Rum 180ml'),('0105','IDL Calypso Gold Red Rum 375ml'),('0104','IDL Calypso Gold Red Rum 750ml'),('0112','IDL Calypso Silver White Rum 180ml'),('0111','IDL Calypso Silver White Rum 375ml'),('0110','IDL Calypso Silver White Rum 750ml'),('0103','IDL Celebration Vodka 375ml'),('0102','IDL Celebration Vodka 750ml'),('0059','IDL Club 7 Arrack 180ml'),('0060','IDL Club 7 Arrack 375ml'),('0061','IDL Club 7 Arrack 750ml'),('0071','IDL Melfort Special Arrack 180ml'),('0070','IDL Melfort Special Arrack 375ml'),('0069','IDL Melfort Special Arrack 750ml'),('0054','IDL Old Arrack 180ml'),('0053','IDL Old Arrack 375ml'),('0052','IDL Old Arrack 750ml'),('0055','IDL Old Arrack 750ml - with Canister'),('0050','IDL Old Reserve Arrack 375ml'),('0049','IDL Old Reserve Arrack 750ml'),('0051','IDL Old Reserve Arrack 750ml - with Canister'),('0068','IDL Red Label Arrack 180ml'),('0067','IDL Red Label Arrack 375ml'),('0066','IDL Red Label Arrack 750ml'),('0093','IDL Ritz Brandy - with Carton 750ml'),('0092','IDL Ritz Brandy 180ml'),('0091','IDL Ritz Brandy 375ml'),('0090','IDL Ritz Brandy 750ml'),('0095','IDL Ritz Cherry Brandy 375ml'),('0094','IDL Ritz Cherry Brandy 750ml'),('0096','IDL Ritz Napoleon Brandy - with Carton 750ml'),('0097','IDL Ritz Napoleon Brandy 750ml'),('0098','IDL V&A Whisky - with Carton 750ml'),('0100','IDL V&A Whisky 180ml'),('0099','IDL V&A Whisky 375ml'),('0101','IDL V&A Whisky 750ml'),('0080','IDL White Diamond - Green Apple Arrack 180ml'),('0079','IDL White Diamond - Green Apple Arrack 375ml'),('0078','IDL White Diamond - Green Apple Arrack 750ml'),('0077','IDL White Diamond - Lemon Arrack 180ml'),('0076','IDL White Diamond - Lemon Arrack 375ml'),('0075','IDL White Diamond - Lemon Arrack 750ml'),('0074','IDL White Diamond Arrack 180ml'),('0073','IDL White Diamond Arrack 375ml'),('0072','IDL White Diamond Arrack 750ml'),('0114','Lion Lager 325ml Bottle'),('0115','Lion Lager 325ml Bottle - Pack of 6'),('0116','Lion Lager 330ml Can '),('0117','Lion Lager 330ml Can - Pack of 6'),('0118','Lion Lager 500ml Can'),('0119','Lion Lager 500ml Can - Pack of 2'),('0113','Lion Lager 625ml Bottle'),('0125','Periceyl Apple Arrack 180ml'),('0124','Periceyl Apple Arrack 375ml'),('0123','Periceyl Apple Arrack 750ml'),('0122','Periceyl Black Opal Arrack 180ml'),('0121','Periceyl Black Opal Arrack 375ml'),('0120','Periceyl Black Opal Arrack 750ml'),('0147','Periceyl Franklin Brandy 180ml'),('0140','Periceyl Franklin Brandy 375ml'),('0139','Periceyl Franklin Brandy 750ml'),('0137','Periceyl Galarie Brandy 375ml'),('0138','Periceyl Galarie Premium Brandy 750ml'),('0136','Periceyl Galerie Brandy 750ml'),('0128','Periceyl Lemon Arrack 180ml'),('0127','Periceyl Lemon Arrack 375ml'),('0126','Periceyl Lemon Arrack 750ml'),('0131','Periceyl mango Arrack 180ml'),('0130','Periceyl Mango Arrack 375ml'),('0129','Periceyl Mango Arrack 750ml'),('0132','Periceyl Passion Fruit Arrack 750ml'),('0150','Periceyl Petroff Vodka 750ml'),('0135','Periceyl Tillsider Whisky 375ml'),('0134','Periceyl Tillsider Whisky 750ml');
-- 
-- INSERT INTO `main_sub_category` VALUES ('01','05','01'),('02','05','02'),('03','05','03'),('04','05','04'),('05','05','05'),('06','05','06'),('07','05','07'),('08','01','08'),('09','01','09'),('10','01','10'),('11','01','11'),('12','01','12'),('13','01','13'),('14','01','14'),('15','01','15'),('16','01','16'),('17','01','17'),('18','01','18'),('19','01','19'),('20','04','20'),('21','04','21'),('22','04','22'),('23','04','23'),('24','02','22'),('25','02','24'),('26','03','25'),('27','03','26'),('28','03','27'),('29','03','28'),('30','03','29'),('31','03','30'),('32','03','31'),('33','03','32'),('34','03','33'),('35','02','34'),('36','02','35'),('37','02','36'),('38','02','37');
-- 
-- INSERT INTO `main_sub_item` VALUES ('00000001','01','0001','050101','',''),('00000002','01','0002','050102','',''),('00000003','01','0003','050103','',''),('00000004','02','0004','050204','',''),('00000005','02','0005','050205','',''),('00000007','02','0006','050206','',''),('00000008','03','0007','050307','',''),('00000009','03','0008','050308','',''),('00000010','07','0009','050709','',''),('00000011','07','0010','050710','',''),('00000012','07','0011','050711','',''),('00000013','06','0012','050612','',''),('00000014','06','0013','050613','',''),('00000015','06','0014','050614','',''),('00000016','06','0015','050615','',''),('00000017','03','0016','050316','',''),('00000018','03','0017','050317','',''),('00000019','04','0018','050418','',''),('00000020','04','0019','050419','',''),('00000021','04','0020','050420','',''),('00000022','05','0021','050521','',''),('00000023','05','0022','050522','',''),('00000024','05','0023','050523','',''),('00000025','08','0024','010824','',''),('00000026','08','0025','010825','',''),('00000027','08','0026','010826','',''),('00000028','09','0027','010927','',''),('00000029','09','0028','010928','',''),('00000030','10','0029','011029','',''),('00000031','11','0030','011130','',''),('00000032','11','0031','011131','',''),('00000033','11','0032','011132','',''),('00000034','12','0033','011233','',''),('00000035','12','0034','011234','',''),('00000036','12','0035','011235','',''),('00000037','13','0036','011336','',''),('00000038','13','0037','011337','',''),('00000039','13','0038','011338','',''),('00000040','14','0039','011439','',''),('00000041','14','0040','011440','',''),('00000042','14','0041','011441','',''),('00000043','15','0042','011542','',''),('00000044','15','0043','011543','',''),('00000045','16','0044','011644','',''),('00000046','16','0045','011645','',''),('00000047','17','0046','011746','',''),('00000048','18','0047','011847','',''),('00000049','19','0048','011948','',''),('00000050','20','0049','042049','',''),('00000051','20','0050','042050','',''),('00000052','20','0051','042051','',''),('00000053','20','0052','042052','',''),('00000054','20','0053','042053','',''),('00000055','20','0054','042054','',''),('00000056','20','0055','042055','',''),('00000057','21','0056','042156','',''),('00000058','21','0057','042157','',''),('00000059','21','0058','042158','',''),('00000060','21','0059','042159','',''),('00000061','21','0060','042160','',''),('00000062','21','0061','042161','',''),('00000063','21','0062','042162','',''),('00000064','21','0063','042163','',''),('00000065','21','0064','042164','',''),('00000066','21','0065','042165','',''),('00000067','21','0066','042166','',''),('00000068','21','0067','042167','',''),('00000069','21','0068','042168','',''),('00000070','22','0069','042269','',''),('00000071','22','0070','042270','',''),('00000072','22','0071','042271','',''),('00000073','22','0072','042272','',''),('00000074','22','0073','042273','',''),('00000075','22','0074','042274','',''),('00000076','22','0075','042275','',''),('00000077','22','0076','042276','',''),('00000078','22','0077','042277','',''),('00000079','22','0078','042278','',''),('00000080','22','0079','042279','',''),('00000081','22','0080','042280','',''),('00000082','23','0081','042381','',''),('00000083','23','0082','042382','',''),('00000084','23','0083','042383','',''),('00000085','23','0084','042384','',''),('00000086','23','0085','042385','',''),('00000087','23','0086','042386','',''),('00000088','23','0087','042387','',''),('00000089','23','0088','042388','',''),('00000090','23','0089','042389','',''),('00000091','23','0090','042390','',''),('00000092','23','0091','042391','',''),('00000093','23','0092','042392','',''),('00000094','23','0093','042393','',''),('00000095','23','0094','042394','',''),('00000096','23','0095','042395','',''),('00000097','23','0096','042396','',''),('00000098','23','0097','042397','',''),('00000099','23','0098','042398','',''),('00000100','23','0099','042399','',''),('00000101','23','0100','0423100','',''),('00000102','23','0101','0423101','',''),('00000103','23','0102','0423102','',''),('00000104','23','0103','0423103','',''),('00000105','23','0104','0423104','',''),('00000106','23','0105','0423105','',''),('00000107','23','0106','0423106','',''),('00000108','23','0107','0423107','',''),('00000109','23','0108','0423108','',''),('00000110','23','0109','0423109','',''),('00000111','23','0110','0423110','',''),('00000112','23','0111','0423111','',''),('00000113','23','0112','0423112','',''),('00000114','26','0113','0325113','',''),('00000115','26','0114','0325114','',''),('00000116','26','0115','0325115','',''),('00000117','26','0116','0325116','',''),('00000118','26','0117','0325117','',''),('00000119','26','0118','0325118','',''),('00000120','26','0119','0325119','',''),('00000121','24','0120','0222120','',''),('00000122','24','0121','0222121','',''),('00000123','24','0122','0222122','',''),('00000124','24','0123','0222123','',''),('00000125','24','0124','0222124','',''),('00000126','24','0125','0222125','',''),('00000127','24','0126','0222126','',''),('00000128','24','0127','0222127','',''),('00000129','24','0128','0222128','',''),('00000130','24','0129','0222129','',''),('00000131','24','0130','0222130','',''),('00000132','24','0131','0222131','',''),('00000133','24','0132','0222132','',''),('00000134','25','0133','0224133','',''),('00000135','25','0134','0224134','',''),('00000136','25','0135','0224135','',''),('00000137','35','0136','0234136','',''),('00000138','35','0137','0234137','',''),('00000139','35','0138','0234138','',''),('00000140','35','0139','0234139','',''),('00000141','35','0140','0234140','',''),('00000142','36','0141','0235141','',''),('00000143','36','0142','0235142','',''),('00000144','36','0143','0235143','',''),('00000145','36','0144','0235144','',''),('00000146','36','0145','0235145','',''),('00000147','36','0146','0235146','',''),('00000148','35','0147','0234147','',''),('00000149','37','0148','0236148','',''),('00000150','37','0149','0236149','',''),('00000151','38','0150','0237150','','');
-- 
-- INSERT INTO `supplier` VALUES ('001','DISTILLERIES COMPANY OF SRI LANKA PLC (DCSL)','110, Norris Canal Rd, Colombo - 10.',NULL,'0115 507000','-','','-','','',NULL),('002','PERICEYL (PVT) LIMITED (PERICEYL)','2 1/1A, High level Road, Colombo - 06',NULL,'0112 515146','-','','-','','',NULL),('003','LION BREWERY (CEYLON) PLC (LION)','254, Colombo Road, Biyagama.',NULL,'0112465900','-','','-','','',NULL),('004','INTERNATIONAL DISTILLERS LIMITED ( IDL)','Melford Estate, Kotalawela,\nKaduwela.',NULL,'0114653400','-','','-','','',NULL),('005','ASIA PACIFIC BREWERY (LANKA) LIMITED - (APBL)','548/B, Nawala Road, Rajagiriya.',NULL,'0114866575','011-2864918','','-','','',NULL);
-- 
-- INSERT INTO `payment` VALUES ('01','CASH'),('02','CHEQUE');

-- CURRENT DATA
/*


INSERT INTO `empty_bottle` VALUES ('BOTTLE-1','30'),('BOTTLE-2','40');
INSERT INTO `main_category` VALUES ('05','APBL'),('01','DCSL'),('09','FLTC'),('04','IDL'),('03','LION'),('06','LUXURY'),('07','MENDIS'),('02','PERICEYL'),('08','ROCKLAND');
INSERT INTO `sub_category` VALUES ('32','3 Coins German Beer'),('07','ABC Stout'),('06','Anchor Smooth'),('03','Anchor Strong'),('22','Arrack'),('04','Baron Super Strong'),('05','Barons Strong'),('42','Beer'),('21','Blended Arrack'),('11','Blue Label'),('34','Brandy'),('28','Calsberg'),('29','Calsberg Special Brew'),('46','Chardonnay'),('43','Cider'),('13','Coconut'),('16','D.D.A'),('10','Economy Special'),('08','Extra Special'),('35','Gin'),('33','Lavina Dark - 30L'),('25','Lion Lager'),('26','Lion Stout'),('23','LMFI'),('18','Narikela'),('14','Old'),('19','Pack 3 in 1'),('39','Premium Coconut Arrack'),('20','Pure Coconut Arrack'),('45','Red Wine'),('40','Regular Coconut Arrack'),('41','Rosaween'),('36','Rum'),('30','Sando Power'),('31','Sando Stout'),('44','Scotch Whisky'),('09','Special'),('17','Sri Lanka'),('27','Strong Beer'),('02','Tiger Black'),('01','Tiger Original'),('47','Trivento'),('38','Ultra Premium Coconut Arrack'),('15','V.S.O.A'),('37','Vodka'),('24','Whisky'),('12','White Label'),('48','White Wine');
INSERT INTO `main_sub_category` VALUES ('01','05','01'),('02','05','02'),('03','05','03'),('04','05','04'),('05','05','05'),('06','05','06'),('07','05','07'),('08','01','08'),('09','01','09'),('10','01','10'),('11','01','11'),('12','01','12'),('13','01','13'),('14','01','14'),('15','01','15'),('16','01','16'),('17','01','17'),('18','01','18'),('19','01','19'),('20','04','20'),('21','04','21'),('22','04','22'),('23','04','23'),('24','02','22'),('25','02','24'),('26','03','25'),('27','03','26'),('28','03','27'),('29','03','28'),('30','03','29'),('31','03','30'),('32','03','31'),('33','03','32'),('34','03','33'),('35','02','34'),('36','02','35'),('37','02','36'),('38','02','37'),('39','07','38'),('40','07','39'),('41','07','40'),('42','07','22'),('43','07','34'),('44','07','24'),('45','07','35'),('46','07','36'),('47','07','37'),('48','07','41'),('49','08','22'),('50','08','24'),('51','08','35'),('52','08','36'),('53','08','34'),('54','08','37'),('55','06','37'),('56','06','24'),('57','06','42'),('58','06','43'),('59','09','44'),('60','09','36'),('61','09','37'),('62','09','45'),('63','09','46'),('64','09','47'),('65','09','48');
INSERT INTO `item` VALUES ('0013','ABPL Anchor Smooth 325ml Bottle'),('0014','ABPL Anchor Smooth 330ml Can'),('0015','ABPL Anchor Smooth 500ml Can'),('0012','ABPL Anchor Smooth 625ml Bottle'),('0008','ABPL Anchor Strong 325ML '),('0016','ABPL Anchor Strong 330ml Can'),('0017','ABPL Anchor Strong 500ml Can'),('0007','ABPL Anchor Strong 625ml Bottle'),('0019','ABPL Baron Super Strong 330ml Can'),('0018','ABPL Baron Super Strong 625ml Bottle'),('0020','ABPL Baron uper Strong 500ml Can'),('0022','ABPL Barons Strong 330ml Can'),('0023','ABPL Barons Strong 500ml Can'),('0021','ABPL Barons Strong 625ml Bottle'),('0010','APBL ABC Stout 330ml Can'),('0011','APBL ABC Stout 500ml Can'),('0009','APBL ABC Stout 625ml Bottle'),('0005','APBL Tiger Black 410ml Bottle'),('0006','APBL Tiger Black 500ml Can'),('0004','APBL Tiger Black 640ml Bottle'),('0002','APBL Tiger Original 410ml Bottle'),('0003','APBL Tiger Original 500ml Can'),('0001','APBL Tiger Original 640ml Bottle'),('0247','Bacardi Superior Rum 750ml'),('0148','Balmora Dark Rum 750ml'),('0149','Balmora White Rum 750ml'),('0179','Barons Napolian Brandy 180ml'),('0178','Barons Napolian Brandy 375ml'),('0177','Barons Napolian Brandy 750ml'),('0182','Blender\'s Choice Whisky 180ml'),('0181','Blender\'s Choice Whisky 375ml'),('0180','Blender\'s Choice Whisky 750ml'),('0249','Carlo Rossi Red Wine 1.5 L'),('0244','Corona Beer 400ml'),('0032','DCSL Blue Label Arrack 180ml'),('0031','DCSL Blue Label Arrack 375ml'),('0030','DCSL Blue Label Arrack 750ml'),('0038','DCSL Coconut Arrack 180ml'),('0037','DCSL Coconut Arrack 375ml'),('0036','DCSL Coconut Arrack 750ml'),('0045','DCSL DDA Arrack 375ml'),('0044','DCSL DDA Arrack 750ml'),('0029','DCSL Economy Special Arrack 180ml'),('0026','DCSL Extra Special Arrack 180ml'),('0025','DCSL Extra Special Arrack 375ml'),('0024','DCSL Extra Special Arrack 750ml'),('0047','DCSL Narikela 750ml'),('0041','DCSL Old Arrack 180ml'),('0040','DCSL Old Arrack 375ml'),('0039','DCSL Old Arrack 750ml'),('0028','DCSL Special Arrack 375ml'),('0027','DCSL Special Arrack 750ml'),('0046','DCSL Sri Lanka Arrack 750ml'),('0043','DCSL VSOA Premium Arrack 375ml'),('0042','DCSL VSOA Premium Arrack 750ml'),('0048','DCSL VSOA/ DDA/ OA 375ml - 3 in1 Pack'),('0035','DCSL White Label Arrack 180ml'),('0034','DCSL White Label Arrack 375ml'),('0033','DCSL White Label Arrack 750ml'),('0246','Dewars White Label Whisky 750ml'),('0173','Empire Mango Arrack 180ml'),('0172','Empire Mango Arrack 375ml'),('0171','Empire Mango Arrack 750ml'),('0176','Empire Strawberry Arrack 180ml'),('0175','Empire Strawberry Arrack 375ml'),('0174','Empire Strawberry Arrack 750ml'),('0248','Finlandia Vodka Regular 750ml'),('0146','Flinton Lemon Gin 180ml'),('0145','Flinton Lemon Gin 375ml'),('0144','Flinton Lemon Gin 750ml'),('0143','Flinton London Dry Gin 180ml'),('0142','Flinton London Dry Gin 375ml'),('0141','Flinton London Dry Gin 750ml'),('0250','Fontera Chardonnay White Wine 750ml'),('0133','House of Tilbury Whisky 750ml'),('0109','IDL Admiral White Rum 180ml'),('0108','IDL Admiral White Rum 375ml'),('0107','IDL Admiral White Rum 750ml'),('0083','IDL Ascot Dry Gin 180ml'),('0082','IDL Ascot Dry Gin 375ml'),('0081','IDL Ascot Dry Gin 750ml'),('0086','IDL Ascot Lemon Gin 180ml'),('0085','IDL Ascot Lemon Gin 375ml'),('0084','IDL Ascot Lemon Gin 750ml'),('0089','IDL Ascot Orange Gin 180ml'),('0088','IDL Ascot Orange Gin 375ml'),('0087','IDL Ascot Orange Gin 750ml'),('0058','IDL Black Label Arrack 180ml'),('0057','IDL Black Label Arrack 375ml'),('0056','IDL Black Label Arrack 750ml'),('0065','IDL Blue Sapphire 180ml'),('0064','IDL Blue Sapphire 375ml'),('0063','IDL Blue Sapphire 750ml'),('0062','IDL Blue Sapphire with Carton 750ml'),('0106','IDL Calypso Gold Red Rum 180ml'),('0105','IDL Calypso Gold Red Rum 375ml'),('0104','IDL Calypso Gold Red Rum 750ml'),('0112','IDL Calypso Silver White Rum 180ml'),('0111','IDL Calypso Silver White Rum 375ml'),('0110','IDL Calypso Silver White Rum 750ml'),('0103','IDL Celebration Vodka 375ml'),('0102','IDL Celebration Vodka 750ml'),('0061','IDL Club 7 Arrack 180ml'),('0060','IDL Club 7 Arrack 375ml'),('0059','IDL Club 7 Arrack 750ml'),('0071','IDL Melfort Special Arrack 180ml'),('0070','IDL Melfort Special Arrack 375ml'),('0069','IDL Melfort Special Arrack 750ml'),('0054','IDL Old Arrack 180ml'),('0053','IDL Old Arrack 375ml'),('0052','IDL Old Arrack 750ml'),('0055','IDL Old Arrack 750ml - with Canister'),('0050','IDL Old Reserve Arrack 375ml'),('0049','IDL Old Reserve Arrack 750ml'),('0051','IDL Old Reserve Arrack 750ml - with Canister'),('0068','IDL Red Label Arrack 180ml'),('0067','IDL Red Label Arrack 375ml'),('0066','IDL Red Label Arrack 750ml'),('0093','IDL Ritz Brandy - with Carton 750ml'),('0092','IDL Ritz Brandy 180ml'),('0091','IDL Ritz Brandy 375ml'),('0090','IDL Ritz Brandy 750ml'),('0095','IDL Ritz Cherry Brandy 375ml'),('0094','IDL Ritz Cherry Brandy 750ml'),('0096','IDL Ritz Napoleon Brandy - with Carton 750ml'),('0097','IDL Ritz Napoleon Brandy 750ml'),('0098','IDL V&A Whisky - with Carton 750ml'),('0100','IDL V&A Whisky 180ml'),('0099','IDL V&A Whisky 375ml'),('0101','IDL V&A Whisky 750ml'),('0080','IDL White Diamond - Green Apple Arrack 180ml'),('0079','IDL White Diamond - Green Apple Arrack 375ml'),('0078','IDL White Diamond - Green Apple Arrack 750ml'),('0077','IDL White Diamond - Lemon Arrack 180ml'),('0076','IDL White Diamond - Lemon Arrack 375ml'),('0075','IDL White Diamond - Lemon Arrack 750ml'),('0074','IDL White Diamond Arrack 180ml'),('0073','IDL White Diamond Arrack 375ml'),('0072','IDL White Diamond Arrack 750ml'),('0187','Island Red Rum 375ml'),('0186','Island Red Rum 750ml'),('0189','Island White Rum 375ml'),('0188','Island White Rum 750ml'),('0159','Lion Carlsberg Beer 325ml Bottle'),('0160','Lion Carlsberg Beer 330ml Can'),('0161','Lion Carlsberg Beer 500ml Can'),('0158','Lion Carlsberg Beer 625ml Bottle'),('0163','Lion Carlsberg Special Brew 325ml Bottle'),('0164','Lion Carlsberg Special Brew 500ml Can'),('0162','Lion Carlsberg Special Brew 625ml Bottle'),('0114','Lion Lager 325ml Bottle'),('0115','Lion Lager 325ml Bottle - Pack of 6'),('0116','Lion Lager 330ml Can '),('0117','Lion Lager 330ml Can - Pack of 6'),('0118','Lion Lager 500ml Can'),('0119','Lion Lager 500ml Can - Pack of 2'),('0113','Lion Lager 625ml Bottle'),('0152','Lion Stout Beer 325ml Bottle'),('0153','Lion Stout Beer 330ml Can'),('0151','Lion Stout Beer 625ml Bottle'),('0155','Lion Strong Beer 325ml Bottle'),('0156','Lion Strong Beer 330ml Can'),('0157','Lion Strong Beer 500ml Can'),('0154','Lion Strong Beer 625ml Bottle'),('0183','London Spice Dry Gin 750ml'),('0185','London Spice Lemon Gin 375ml'),('0184','London Spice Lemon Gin 750ml'),('0167','Mendis Original Arrack 180ml'),('0166','Mendis Original Arrack 375ml'),('0165','Mendis Original Arrack 750ml'),('0170','Mendis Original White Arrack 180ml'),('0169','Mendis Original White Arrack 375ml'),('0168','Mendis Original White Arrack 750ml'),('0125','Periceyl Apple Arrack 180ml'),('0124','Periceyl Apple Arrack 375ml'),('0123','Periceyl Apple Arrack 750ml'),('0122','Periceyl Black Opal Arrack 180ml'),('0121','Periceyl Black Opal Arrack 375ml'),('0120','Periceyl Black Opal Arrack 750ml'),('0147','Periceyl Franklin Brandy 180ml'),('0140','Periceyl Franklin Brandy 375ml'),('0139','Periceyl Franklin Brandy 750ml'),('0137','Periceyl Galarie Brandy 375ml'),('0138','Periceyl Galarie Premium Brandy 750ml'),('0136','Periceyl Galerie Brandy 750ml'),('0128','Periceyl Lemon Arrack 180ml'),('0127','Periceyl Lemon Arrack 375ml'),('0126','Periceyl Lemon Arrack 750ml'),('0131','Periceyl mango Arrack 180ml'),('0130','Periceyl Mango Arrack 375ml'),('0129','Periceyl Mango Arrack 750ml'),('0132','Periceyl Passion Fruit Arrack 750ml'),('0150','Periceyl Petroff Vodka 750ml'),('0135','Periceyl Tillsider Whisky 375ml'),('0134','Periceyl Tillsider Whisky 750ml'),('0207','Rockland 3-Star Gold Blend Arrack 180ml'),('0206','Rockland 3-Star Gold Blend Arrack 375ml'),('0205','Rockland 3-Star Gold Blend Arrack 750ml'),('0222','Rockland Dry Gin 180ml'),('0221','Rockland Dry Gin 375ml'),('0220','Rockland Dry Gin 750ml'),('0213','Rockland English Apple Arrak 180ml'),('0212','Rockland English Apple Arrak 375ml'),('0211','Rockland English Apple Arrak 750ml'),('0208','Rockland Governer\'s Choice Arrack 750ml'),('0210','Rockland Governer;s Choice Arrack 180ml'),('0203','Rockland Halmilla Old Arrack 750ml'),('0234','Rockland Hanappier Brandy 180ml'),('0233','Rockland Hanappier Brandy 375ml'),('0240','Rockland Keruv Vodka 375ml'),('0239','Rockland Keruv Vodka 750ml'),('0225','Rockland Lemon Gin 180ml'),('0224','Rockland Lemon Gin 375ml'),('0223','Rockland Lemon Gin 750ml'),('0202','Rockland Old Arrack 180ml'),('0201','Rockland Old Arrack 375ml'),('0200','Rockland Old Arrack 750ml'),('0199','Rockland Old Arrack Ex.Strong 180ml'),('0198','Rockland Old Arrack Ex.Strong 375ml'),('0197','Rockland Old Arrack Ex.Strong 750ml'),('0235','Rockland Old Keg D/Blend Whisky 750ml'),('0238','Rockland Old Keg Whisky 180ml'),('0237','Rockland Old Keg Whisky 375ml'),('0236','Rockland Old Keg Whisky 750ml'),('0204','Rockland Paradise White Coconut Arrack 750ml'),('0216','Rockland Premium Grape Arrak 180ml'),('0215','Rockland Premium Grape Arrak 375ml'),('0214','Rockland Premium Grape Arrak 750ml'),('0228','Rockland Premium Red Rum 180ml'),('0227','Rockland Premium Red Rum 375ml'),('0226','Rockland Premium Red Rum 750ml'),('0231','Rockland Premium White Rum 180ml'),('0230','Rockland Premium White Rum 375ml'),('0229','Rockland Premium White Rum 750ml'),('0219','Rockland Tropical Mango Arrak 180ml'),('0217','Rockland Tropical Mango Arrak 750ml'),('0194','Rockland Vintage Xtra Arrack 180ml'),('0193','Rockland Vintage Xtra Arrack 375ml'),('0192','Rockland Vintage Xtra Arrack 750ml'),('0209','Rocland Governer\'s Choice Arrack 375ml'),('0218','Rocland Tropical Mango Arrak 375ml'),('0232','Roclkland Hanappier Brandy 750ml'),('0191','Rosaween Coffee Liqueur 375ml'),('0190','Rusalka Vodka 750ml'),('0241','Smirnoff Red Vodka 1000ml'),('0245','Somerby Apple Cider 355ml'),('0251','Trivento Pasos De Tango Red Wine 750ml'),('0243','VAT 69 Whisky 375ml'),('0242','VAT 69 Whisky 750ml'),('0196','VAT 9 Special Reserve Arrack 750ml'),('0195','VAT 9 Special Reserve Arrack with Box 750ml');
INSERT INTO `main_sub_item` VALUES ('00000001','01','0001','050101','',''),('00000002','01','0002','050102','',''),('00000003','01','0003','050103','',''),('00000004','02','0004','050204','',''),('00000005','02','0005','050205','',''),('00000007','02','0006','050206','',''),('00000008','03','0007','050307','','abc'),('00000009','03','0008','050308','',''),('00000010','07','0009','050709','','4796009867367'),('00000011','07','0010','050710','','333'),('00000012','07','0011','050711','',''),('00000013','06','0012','050612','',''),('00000014','06','0013','050613','',''),('00000015','06','0014','050614','',''),('00000016','06','0015','050615','',''),('00000017','03','0016','050316','',''),('00000018','03','0017','050317','',''),('00000019','04','0018','050418','',''),('00000020','04','0019','050419','',''),('00000021','04','0020','050420','',''),('00000022','05','0021','050521','',''),('00000023','05','0022','050522','',''),('00000024','05','0023','050523','',''),('00000025','08','0024','010824','',''),('00000026','08','0025','010825','',''),('00000027','08','0026','010826','',''),('00000028','09','0027','010927','',''),('00000029','09','0028','010928','',''),('00000030','10','0029','011029','',''),('00000031','11','0030','011130','',''),('00000032','11','0031','011131','',''),('00000033','11','0032','011132','',''),('00000034','12','0033','011233','',''),('00000035','12','0034','011234','',''),('00000036','12','0035','011235','',''),('00000037','13','0036','011336','',''),('00000038','13','0037','011337','',''),('00000039','13','0038','011338','',''),('00000040','14','0039','011439','',''),('00000041','14','0040','011440','',''),('00000042','14','0041','011441','',''),('00000043','15','0042','011542','',''),('00000044','15','0043','011543','',''),('00000045','16','0044','011644','',''),('00000046','16','0045','011645','',''),('00000047','17','0046','011746','',''),('00000048','18','0047','011847','',''),('00000049','19','0048','011948','',''),('00000050','20','0049','042049','',''),('00000051','20','0050','042050','',''),('00000052','20','0051','042051','',''),('00000053','20','0052','042052','',''),('00000054','20','0053','042053','',''),('00000055','20','0054','042054','',''),('00000056','20','0055','042055','',''),('00000057','21','0056','042156','',''),('00000058','21','0057','042157','',''),('00000059','21','0058','042158','',''),('00000060','21','0059','042159','',''),('00000061','21','0060','042160','',''),('00000062','21','0061','042161','',''),('00000063','21','0062','042162','',''),('00000064','21','0063','042163','',''),('00000065','21','0064','042164','',''),('00000066','21','0065','042165','',''),('00000067','21','0066','042166','',''),('00000068','21','0067','042167','',''),('00000069','21','0068','042168','',''),('00000070','22','0069','042269','',''),('00000071','22','0070','042270','',''),('00000072','22','0071','042271','',''),('00000073','22','0072','042272','',''),('00000074','22','0073','042273','',''),('00000075','22','0074','042274','',''),('00000076','22','0075','042275','',''),('00000077','22','0076','042276','',''),('00000078','22','0077','042277','',''),('00000079','22','0078','042278','',''),('00000080','22','0079','042279','',''),('00000081','22','0080','042280','',''),('00000082','23','0081','042381','',''),('00000083','23','0082','042382','',''),('00000084','23','0083','042383','',''),('00000085','23','0084','042384','',''),('00000086','23','0085','042385','',''),('00000087','23','0086','042386','',''),('00000088','23','0087','042387','',''),('00000089','23','0088','042388','',''),('00000090','23','0089','042389','',''),('00000091','23','0090','042390','',''),('00000092','23','0091','042391','',''),('00000093','23','0092','042392','',''),('00000094','23','0093','042393','',''),('00000095','23','0094','042394','',''),('00000096','23','0095','042395','',''),('00000097','23','0096','042396','',''),('00000098','23','0097','042397','',''),('00000099','23','0098','042398','',''),('00000100','23','0099','042399','',''),('00000101','23','0100','0423100','',''),('00000102','23','0101','0423101','',''),('00000103','23','0102','0423102','',''),('00000104','23','0103','0423103','',''),('00000105','23','0104','0423104','',''),('00000106','23','0105','0423105','',''),('00000107','23','0106','0423106','',''),('00000108','23','0107','0423107','',''),('00000109','23','0108','0423108','',''),('00000110','23','0109','0423109','',''),('00000111','23','0110','0423110','',''),('00000112','23','0111','0423111','',''),('00000113','23','0112','0423112','',''),('00000114','26','0113','0325113','',''),('00000115','26','0114','0325114','',''),('00000116','26','0115','0325115','',''),('00000117','26','0116','0325116','',''),('00000118','26','0117','0325117','',''),('00000119','26','0118','0325118','',''),('00000120','26','0119','0325119','',''),('00000121','24','0120','0222120','',''),('00000122','24','0121','0222121','',''),('00000123','24','0122','0222122','',''),('00000124','24','0123','0222123','',''),('00000125','24','0124','0222124','',''),('00000126','24','0125','0222125','',''),('00000127','24','0126','0222126','',''),('00000128','24','0127','0222127','',''),('00000129','24','0128','0222128','',''),('00000130','24','0129','0222129','',''),('00000131','24','0130','0222130','',''),('00000132','24','0131','0222131','',''),('00000133','24','0132','0222132','',''),('00000134','25','0133','0224133','',''),('00000135','25','0134','0224134','',''),('00000136','25','0135','0224135','',''),('00000137','35','0136','0234136','',''),('00000138','35','0137','0234137','',''),('00000139','35','0138','0234138','',''),('00000140','35','0139','0234139','',''),('00000141','35','0140','0234140','',''),('00000142','36','0141','0235141','',''),('00000143','36','0142','0235142','',''),('00000144','36','0143','0235143','',''),('00000145','36','0144','0235144','',''),('00000146','36','0145','0235145','',''),('00000147','36','0146','0235146','',''),('00000148','35','0147','0234147','',''),('00000149','37','0148','0236148','',''),('00000150','37','0149','0236149','',''),('00000151','38','0150','0237150','',''),('00000152','27','0151','0326151','',''),('00000153','27','0152','0326152','',''),('00000154','27','0153','0326153','',''),('00000155','28','0154','0327154','',''),('00000156','28','0155','0327155','',''),('00000157','28','0156','0327156','',''),('00000158','28','0157','0327157','',''),('00000159','29','0158','0328158','',''),('00000160','29','0159','0328159','',''),('00000161','29','0160','0328160','',''),('00000162','29','0161','0328161','',''),('00000163','30','0162','0329162','',''),('00000164','30','0163','0329163','',''),('00000165','30','0164','0329164','',''),('00000166','42','0165','0722165','',''),('00000167','42','0166','0722166','',''),('00000168','42','0167','0722167','',''),('00000169','42','0168','0722168','',''),('00000170','42','0169','0722169','',''),('00000171','42','0170','0722170','',''),('00000172','42','0171','0722171','',''),('00000173','42','0172','0722172','',''),('00000174','42','0173','0722173','',''),('00000175','42','0174','0722174','',''),('00000176','42','0175','0722175','',''),('00000177','42','0176','0722176','',''),('00000178','43','0177','0734177','',''),('00000179','43','0178','0734178','',''),('00000180','43','0179','0734179','',''),('00000181','44','0180','0724180','',''),('00000182','44','0181','0724181','',''),('00000183','44','0182','0724182','',''),('00000184','45','0183','0735183','',''),('00000185','45','0184','0735184','',''),('00000186','45','0185','0735185','',''),('00000187','46','0186','0736186','',''),('00000188','46','0187','0736187','',''),('00000189','46','0188','0736188','',''),('00000190','46','0189','0736189','',''),('00000191','47','0190','0737190','',''),('00000192','48','0191','0741191','',''),('00000193','49','0192','0822192','',''),('00000194','49','0193','0822193','',''),('00000195','49','0194','0822194','',''),('00000196','49','0195','0822195','',''),('00000197','49','0196','0822196','',''),('00000198','49','0197','0822197','',''),('00000199','49','0198','0822198','',''),('00000200','49','0199','0822199','',''),('00000201','49','0200','0822200','',''),('00000202','49','0201','0822201','',''),('00000203','49','0202','0822202','',''),('00000204','49','0203','0822203','',''),('00000205','49','0204','0822204','',''),('00000206','49','0205','0822205','',''),('00000207','49','0206','0822206','',''),('00000208','49','0207','0822207','',''),('00000209','49','0208','0822208','',''),('00000210','49','0209','0822209','',''),('00000211','49','0210','0822210','',''),('00000212','49','0211','0822211','',''),('00000213','49','0212','0822212','',''),('00000214','49','0213','0822213','',''),('00000215','49','0214','0822214','',''),('00000216','49','0215','0822215','',''),('00000217','49','0216','0822216','',''),('00000218','49','0217','0822217','',''),('00000219','49','0218','0822218','',''),('00000220','49','0219','0822219','',''),('00000221','51','0220','0835220','',''),('00000222','51','0221','0835221','',''),('00000223','51','0222','0835222','',''),('00000224','51','0223','0835223','',''),('00000225','51','0224','0835224','',''),('00000226','51','0225','0835225','',''),('00000227','52','0226','0836226','',''),('00000228','52','0227','0836227','',''),('00000229','52','0228','0836228','',''),('00000230','52','0229','0836229','',''),('00000231','52','0230','0836230','',''),('00000232','52','0231','0836231','',''),('00000233','53','0232','0834232','',''),('00000234','53','0233','0834233','',''),('00000235','53','0234','0834234','',''),('00000236','50','0235','0824235','',''),('00000237','50','0236','0824236','',''),('00000238','50','0237','0824237','',''),('00000239','50','0238','0824238','',''),('00000240','54','0239','0837239','',''),('00000241','54','0240','0837240','',''),('00000242','55','0241','0637241','',''),('00000243','56','0242','0624242','',''),('00000244','56','0243','0624243','',''),('00000245','57','0244','0642244','',''),('00000246','58','0245','0643245','',''),('00000247','59','0246','0944246','',''),('00000248','60','0247','0936247','',''),('00000249','61','0248','0937248','',''),('00000250','62','0249','0945249','',''),('00000251','65','0250','0948250','',''),('00000252','62','0251','0945251','','');
INSERT INTO `supplier` VALUES ('001','DISTILLERIES COMPANY OF SRI LANKA PLC (DCSL)','110, Norris Canal Rd, Colombo - 10.',NULL,'0115 507000','-','','-','','',NULL),('002','PERICEYL (PVT) LIMITED (PERICEYL)','2 1/1A, High level Road, Colombo - 06',NULL,'0112 515146','-','','-','','',NULL),('003','LION BREWERY (CEYLON) PLC (LION)','254, Colombo Road, Biyagama.','mmm','0112465900','-','nbnn','-','abc','071','2016-09-29'),('004','INTERNATIONAL DISTILLERS LIMITED ( IDL)','Melford Estate, Kotalawela,\nKaduwela.',NULL,'0114653400','-','','-','','',NULL),('005','ASIA PACIFIC BREWERY (LANKA) LIMITED - (APBL)','548/B, Nawala Road, Rajagiriya.',NULL,'0114866575','011-2864918','','-','','',NULL),('006','LUXURY BRANDS (PVT) LTD (LUXURY)','254, Colombo Road,\nBiyagama.','','2465900,','011-2465901','','011-2465901','','','2016-09-25'),('007','W. M. MENDIS & CO. LTD (MENDIS)','309/5, Negombo Road, Welisara','','4692323','011-7723513','','011-7723513','','','2016-09-25'),('008','ROCKLAND DISTILLERIES (PVT) LTD (ROCLAND)','160/24, Kirimandala Mw,\nColombo - 05.','','4426100','011-2553983','','011-2553983','','','2016-09-25'),('009','FREE LANKA TRADING  COMPANY (PVT) LTD (FLTC)','Level 3, Prince Alfred Tower,\nNo:10, Alfred House Gardens,\nColombo - 03.','sales@freelanka.com','4523600','011-2552140','','011-2552140','','','2016-09-25');
INSERT INTO `payment` VALUES ('01','CASH'),('02','CHEQUE');


*/




