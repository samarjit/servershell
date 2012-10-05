CREATE TABLE test(name varchar(20), bday timestamp);
create table test2(name varchar(20), bday date);
CREATE   TABLE  PROGRAMDETAILS(PROGNAME VARCHAR(20),PROGDESC VARCHAR(30), issuername varchar(20),COUNTRY_ISSUE VARCHAR(30), STATUS VARCHAR(30));

INSERT INTO TEST VALUES('sam','2010-12-12 00:00:00.000000')                        ;
INSERT INTO TEST2 VALUES('sam','2010-12-12')                                       ;
INSERT INTO TEST2 VALUES('sam sam','2011-02-14')                                   ;
INSERT INTO  PROGRAMDETAILS(PROGNAME, PROGDESC, ISSUERNAME, COUNTRY_ISSUE) VALUES
('NewProg', 'NwProg desc', NULL, 'SINGAPORE'),
('NewProg', 'NwProg desc', NULL, 'SINGAPORE'),
('NewProg', 'NwProg desc', NULL, 'SINGAPORE'),
('NewProg', 'NwProg desc', NULL, 'SINGAPORE'),
('NewCard', 'Some desc', NULL, 'AUSTRALIA'),
('TRACARD', 'Travel Card Program', NULL, 'INDIA'),
('NewCard', 'Some desc', NULL, 'AUSTRALIA'),
('LOYCARD', 'Loyalty Card Program', NULL, 'PHILIPPINES');



drop alias if exists TO_CHAR; 
create alias TO_CHAR as $$ 
import java.text.SimpleDateFormat;
import java.util.Date;
@CODE
  String toChar(String date, String pattern) throws Exception { 
	pattern = pattern.replaceAll("YY","yy");
	pattern = pattern.replaceAll("DD","dd");
	pattern = pattern.replaceAll("HH24|hh24","HH");
	pattern = pattern.replaceAll("HH?!24|hh?!24","KK");
	pattern = pattern.replaceAll("MON|mon","MMM");
	pattern = pattern.replaceAll("MM|mm","MM");
	pattern = pattern.replaceAll("MI|mi","mm");
	pattern = pattern.replaceAll("SS|ss","ss");
	pattern = pattern.replaceAll("AM|PM","aa");
	System.out.println(pattern);
	SimpleDateFormat sm = new SimpleDateFormat(pattern);
	java.util.Date dt; 
		if(date.length() > 10)dt = java.sql.Timestamp.valueOf(date);
		else
		 dt = java.sql.Date.valueOf(date);
		return sm.format(dt); 
 }
$$;


create table user_role_map(userid varchar2(20),roleid varchar2(20));
insert into user_role_map values('sam_admin','ADMIN');
insert into user_role_map values('sam_man','MANAGER');
insert into user_role_map values('sam_user','USER');
insert into user_role_map values('sam_man_user','USER');
insert into user_role_map values('sam_man_user','MANAGER');

create table locale(lang_name varchar2(50), lang_code varchar2(25) primary key);
insert into locale(lang_name, lang_code)values ('English','en'),('Chinese','zh_CN'),('German','de'),('France','fr');


create table country (country_code varchar2(10), country_name varchar2(30), currency_code varchar2(10),currency varchar2(30));

insert into country (country_code,country_name,currency_code,currency) values ('AFG','Afghanistan','AFA','Afghani');
insert into country (country_code,country_name,currency_code,currency) values ('ARG','Argentina','ARP','Peso');
insert into country (country_code,country_name,currency_code,currency) values ('AUS','Australia','AUD','Australian Dollar');
insert into country (country_code,country_name,currency_code,currency) values ('BGD','Bangladesh','BDT','Taka');
insert into country (country_code,country_name,currency_code,currency) values ('BEL','Belgium','EUR','Euro');
insert into country (country_code,country_name,currency_code,currency) values ('BTN','Bhutan','INR','Indian Rupee');
insert into country (country_code,country_name,currency_code,currency) values ('BRA','Brazil','BRR','Brazil');
insert into country (country_code,country_name,currency_code,currency) values ('CAN','Canada','CAD','Dollar');
insert into country (country_code,country_name,currency_code,currency) values ('CHN','China','CNY','Yuan Renminbi');
insert into country (country_code,country_name,currency_code,currency) values ('COL','Colombia','COP','Peso');
insert into country (country_code,country_name,currency_code,currency) values ('CUB','Cuba','CUP','Cuban Peso');
insert into country (country_code,country_name,currency_code,currency) values ('DNK','Denmark','DKK','Danish Krone');
insert into country (country_code,country_name,currency_code,currency) values ('EGY','Egypt','EGP','Egyptian Pound');
insert into country (country_code,country_name,currency_code,currency) values ('FJI','Fiji','FJD','Fijian Dollar');
insert into country (country_code,country_name,currency_code,currency) values ('FRA','France','EUR','Euro');
insert into country (country_code,country_name,currency_code,currency) values ('DEU','Germany','EUR','Euro');
insert into country (country_code,country_name,currency_code,currency) values ('HKG','Hong Kong','HKD','Hong Kong Dollar');
insert into country (country_code,country_name,currency_code,currency) values ('IND','India','INR','Indian Rupee');
insert into country (country_code,country_name,currency_code,currency) values ('IDN','Indonesia','IDR','Indonesian Rupiah');
insert into country (country_code,country_name,currency_code,currency) values ('IRN','Iran ','IRR','Iranian Rial');
insert into country (country_code,country_name,currency_code,currency) values ('IRQ','Iraq','IQD','Iraqi Dinar');
insert into country (country_code,country_name,currency_code,currency) values ('IRL','Ireland','EUR','Euro');
insert into country (country_code,country_name,currency_code,currency) values ('ISR','Israel','ILS','Shekel');
insert into country (country_code,country_name,currency_code,currency) values ('ITA','Italy','EUR','Euro');
insert into country (country_code,country_name,currency_code,currency) values ('JPN','Japan','JPY','Yen');
insert into country (country_code,country_name,currency_code,currency) values ('KEN','Kenya','KES','Kenyan Shilling');
insert into country (country_code,country_name,currency_code,currency) values ('KWT','Kuwait','KWD','Kuwaiti Dinar');
insert into country (country_code,country_name,currency_code,currency) values ('MYS','Malaysia','MYR','Ringgit');
insert into country (country_code,country_name,currency_code,currency) values ('MDV','Maldives','MVR','Rufiyaa');
insert into country (country_code,country_name,currency_code,currency) values ('MEX','Mexico','MXP','Peso');
insert into country (country_code,country_name,currency_code,currency) values ('NPL','Nepal','NPR','Nepalese Rupee');
insert into country (country_code,country_name,currency_code,currency) values ('NLD','Netherlands','EUR','Euro');
insert into country (country_code,country_name,currency_code,currency) values ('NZL','New Zealand','NZD','New Zealand Dollar');
insert into country (country_code,country_name,currency_code,currency) values ('OMN','Oman','OMR','Sul Rial');
insert into country (country_code,country_name,currency_code,currency) values ('PAK','Pakistan','PKR','Rupee');
insert into country (country_code,country_name,currency_code,currency) values ('PHL','Philippines','PHP','Peso');
insert into country (country_code,country_name,currency_code,currency) values ('RUS','Russian Federation','RUR','Ruble');
insert into country (country_code,country_name,currency_code,currency) values ('SAU','Saudi Arabia','SAR','Riyal');
insert into country (country_code,country_name,currency_code,currency) values ('SGP','Singapore','SGD','Dollar');
insert into country (country_code,country_name,currency_code,currency) values ('ZAF','South Africa','ZAR','Rand');
insert into country (country_code,country_name,currency_code,currency) values ('ESP','Spain','EUR','Euro');
insert into country (country_code,country_name,currency_code,currency) values ('LKA','Sri Lanka','LKR','Rupee');
insert into country (country_code,country_name,currency_code,currency) values ('SWE','Sweden','SEK','Krona');
insert into country (country_code,country_name,currency_code,currency) values ('CHE','Switzerland','CHF','Swiss Franc');
insert into country (country_code,country_name,currency_code,currency) values ('TWN','Taiwan','TWD','Dollar');
insert into country (country_code,country_name,currency_code,currency) values ('THA','Thailand','THB','Baht');
insert into country (country_code,country_name,currency_code,currency) values ('GBR','United Kingdom','GBP','Pound Sterling');
insert into country (country_code,country_name,currency_code,currency) values ('USA','United States','USD','US Dollar');
insert into country (country_code,country_name,currency_code,currency) values ('VNM','Vietnam','VND','Dong');


create table program_setup(prog_code varchar2(10),prog_name varchar2(25),prog_desc varchar2(50),issuer_name varchar2(25), country varchar2(25),status varchar2(16),maker_id varchar2(25),maker_date date);
insert into program_setup (PROG_CODE,PROG_NAME,PROG_DESC,ISSUER_NAME,COUNTRY,STATUS,MAKER_ID,MAKER_DATE) values ('PM001','Loyalty card','loyalty card desc','HDFC','IND','','ram','2011-02-02');
insert into program_setup (PROG_CODE,PROG_NAME,PROG_DESC,ISSUER_NAME,COUNTRY,STATUS,MAKER_ID,MAKER_DATE) values ('PM002','Travel card','Travel card desc','DBS','SGP','','Srini','2011-02-14');
insert into program_setup (PROG_CODE,PROG_NAME,PROG_DESC,ISSUER_NAME,COUNTRY,STATUS,MAKER_ID,MAKER_DATE) values ('PM003','Gift card','Gift card desc','ICICI','IND','','Gokul','2011-02-06');
insert into program_setup (PROG_CODE,PROG_NAME,PROG_DESC,ISSUER_NAME,COUNTRY,STATUS,MAKER_ID,MAKER_DATE) values ('PM004','Salary card','Salary card desc','OCBC','USA','','Ravi','2011-02-22');


create table bin_group_details(bin_group_code varchar2(10),bin_group_name varchar2(25),bin_group_desc varchar2(50),bin_currency varchar2(25),settlement_currency varchar2(25), issuer_name varchar2(25),issue_country varchar2(25),
status varchar2(16),maker_id varchar2(25),maker_date date,bin_range_begin varchar2(6),bin_range_from number(9),bin_range_to number(9),total_num_cards number(10));
insert into BIN_GROUP_DETAILS (BIN_GROUP_NAME ,BIN_GROUP_DESC ,BIN_CURRENCY ,SETTLEMENT_CURRENCY ,issuer_name,ISSUE_COUNTRY ,STATUS ,MAKER_ID ,MAKER_DATE ,bin_range_begin ,BIN_RANGE_FROM ,BIN_RANGE_TO ,TOTAL_NUM_CARDS )values('BINGRP1','BINGRP1 desc','SGD','SGD','DBS BANK','SGP','','ram',sysdate,'111111',000000100,000002000,1900);
insert into BIN_GROUP_DETAILS (BIN_GROUP_NAME ,BIN_GROUP_DESC ,BIN_CURRENCY ,SETTLEMENT_CURRENCY ,issuer_name,ISSUE_COUNTRY ,STATUS ,MAKER_ID ,MAKER_DATE ,bin_range_begin,BIN_RANGE_FROM ,BIN_RANGE_TO ,TOTAL_NUM_CARDS )values('BINGRP2','BINGRP2 desc','INR','INR','HDFC BANK','IND','','Srini',sysdate,'222222',000000100,000000999,899);


create table role_rights_map (role varchar(20), menu_id varchar(20));
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','tab1');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','tab1menu1');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','tab1menu4');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','tab1menu5');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','tab1menu6');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu1');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','tab1menu2');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu2');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu3');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu4');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu5');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu6');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu7');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','tab1menu3');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu8');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu9');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu10');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','tab2');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','tab2menu1');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu11');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu12');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu13');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','tab3');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','tab3menu1');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu14');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu15');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu16');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','tab4');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','tab4menu1');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu17');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu18');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu19');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','tab5');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','tab5menu1');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu20');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu21');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('ADMIN','submenu22');

insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('MANAGER','tab1');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('MANAGER','tab1menu1');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('MANAGER','submenu1');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('MANAGER','tab1menu2');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('MANAGER','submenu2');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('MANAGER','submenu3');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('MANAGER','submenu4');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('MANAGER','submenu5');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('MANAGER','submenu6');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('MANAGER','submenu7');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('MANAGER','tab1menu3');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('MANAGER','submenu8');

insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('MANAGER','tab2');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('MANAGER','tab2menu1');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('MANAGER','submenu11');

insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('USER','tab3');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('USER','tab3menu1');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('USER','submenu14');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('USER','submenu15');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('USER','submenu16');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('USER','tab4');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('USER','tab4menu1');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('USER','submenu17');

insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('USER','tab5');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('USER','tab5menu1');
insert into ROLE_RIGHTS_MAP (ROLE ,MENU_ID ) values('USER','submenu20');

create table product_details(product_code varchar2(16) primary key , product_name varchar2(100),plastic_code varchar2(100),plastic_desc varchar2(100));


insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('TEST01','TESTING','FEVCUS-FEVO Customer design','FEVO Customer design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EMV083','EMV Generic Product','EMVBLK-EMV Black(unprinted)','EMV Black(unprinted)');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EMV040','EMV Reloadable Generic','EMVBLK-EMV Black(unprinted)','EMV Black(unprinted)');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EMV093','EMV Reloadable SE OTC','EMVBLK-EMV Black(unprinted)','EMV Black(unprinted)');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EMV081','EMV081','FEVCUS-FEVO Customer design','FEVO Customer design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EMV085','EMV RLDB Corporate','EMVBLK4-EMV RLDB Corporate','EMV RLDB Corporate');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EMV086','RLDB Bulk','EMVBLK5-RLDB Bulk','RLDB Bulk');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EMV087','NFC','EMV-NFC','NFC');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EMV094','EMV GIFT OTC','EMVBLK-EMV Black(unprinted)','EMV Black(unprinted)');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('FEVO04','Standard RoadShow Gift','FEVSTD-FEVO Standard design','FEVO Standard design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('FEGTCU','FEVO Gift card Customized','FEVCUS-FEVO Customer design','FEVO Customer design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('FEGTST','FEVO Gift card Standard','FEVSTD-FEVO Standard design','FEVO Standard design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('FERSCU','FEVO Road show Customized','FEVWHT-FEVO White (unprinted) design','FEVO White (unprinted) design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('FEVOSP','FEVO Special Edition','EZLSTD-EZL Standard design','EZL Standard design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EZLCUS','Personalised FEVO with ez-link','EZLWHT-EZL Standard design','EZL Standard design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EZLSPS','FEVO Special Edition','EZLSTD-EZL Standard design','EZL Standard design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('ECLC52','Fevo W EZL customised','EZLSTD-EZLINK Standard design','EZLINK Standard design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EZLS56','OTC FEVO w EZL Customized','EZLWHT-EZLINK Custom design','EZLINK Custom design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EZLS21','SPECIAL EDITION - FEVO','FEVWHT-FEVO White (unprinted) design','FEVO White (unprinted) design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EZLS53','Road show FEVO w EZL Standard','EZLSTD-EZLINK Standard design','EZLINK Standard design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EZLC54','Road show FEVO w EZL Customised','EZLWHT-EZLINK Custom design','EZLINK Custom design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('FEVO02','Customised Online Gift','EZLSTD-EZLINK Standard design','EZLINK Standard design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('FEVO01','Standard Online Gift','FEVSTD-FEVO Standard design|FEVO White (unprinted) design','FEVO Standard design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('ECLC51','Fevo W EZL standard','FEVSTD-FEVO Standard design|FEVO White (unprinted) design','FEVO Standard design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EZLS22','SPECIAL EDITION - EZL','EZLWHT-EZLINK Custom design','EZLINK Custom design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('FEVO03','Customised RoadShow Gift','FEVCUS-FEVO Customer design','FEVO Customer design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EZONLI','EZONLI','EZLSTD-EZLINK Standard design','EZLINK Standard design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EZLS55','OTC FEVO w EZL Standard','EZLSTD-EZLINK Standard design','EZLINK Standard design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EZLS24','OTC SPECIAL EDITION - EZL','EZLWHT-EZLINK Custom design','EZLINK Custom design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EPFEVO','EPFEVO MAIN','FEVSTD-FEVO Standard design','FEVO Standard design');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EMV082','EMV Generic','EMVBLK-EMV Black(unprinted)','EMV Black(unprinted)');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EMV084','EMV084 Generic Product Type','EMVBLK-EMV Black(unprinted)','EMV Black(unprinted)');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EMV091','EMV Reloadable Generic','EMVBLK-EMV Black(unprinted)','EMV Black(unprinted)');
insert into product_details (product_code ,product_name ,plastic_code ,plastic_desc ) values('EMV092','EMV Reloadable SE','EMVBLK-EMV Black(unprinted)','EMV Black(unprinted)');

create table process_Status(process_id number(2), process_name varchar2(25));

insert into  process_Status (process_id,process_name)values(1,'CARD ORDER');
insert into  process_Status (process_id,process_name)values(2,'AUTHORIZATION');
insert into  process_Status (process_id,process_name)values(3,'GENERATE CARD');
insert into  process_Status (process_id,process_name)values(4,'EMBOSSING');
insert into  process_Status (process_id,process_name)values(5,'DISPATCH');

create table card_order(invoice_no number primary key,product_code varchar2(16),product_name varchar2(100),plastic_code varchar2(100),plastic_desc varchar2(100),ordered_quantity number(10), ordered_by varchar2(50),ordered_date date,authorize_status varchar2(25),embossing_status varchar2(25),process_status number(2));
create sequence CARD_ORDER_seq START WITH 111116 INCREMENT BY 1;

create table card_entry (card_no number(20) primary key ,invoice_no number, pin_number varchar2(16),cvv number(6));
create sequence card_no_seq START WITH 4444333322221111 INCREMENT BY 1;

INSERT INTO CARD_ORDER(INVOICE_NO, PRODUCT_CODE, PRODUCT_NAME, PLASTIC_CODE, PLASTIC_DESC, ORDERED_QUANTITY, ORDERED_BY, ORDERED_DATE, AUTHORIZE_STATUS, EMBOSSING_STATUS, PROCESS_STATUS) VALUES
(111111, 'EMV083', 'EMV Generic Product', 'EMVBLK-EMV Black(unprinted)', 'EMV Black(unprinted)', 50, 'user', DATE '2011-07-19', 'APPROVED', 'PROGRESS', 3),
(111112, 'EMV085', 'EMV RLDB Corporate', 'EMVBLK4-EMV RLDB Corporate', 'EMV RLDB Corporate', 75, 'user', DATE '2011-07-19', 'APPROVED', 'PROGRESS', 3),
(111113, 'EMV040', 'EMV Reloadable Generic', 'EMVBLK-EMV Black(unprinted)', 'EMV Black(unprinted)', 100, 'user', DATE '2011-07-19', 'APPROVED', 'PROGRESS', 2),
(111114, 'FEGTST', 'FEVO Gift card Standard', 'FEVSTD-FEVO Standard design', 'FEVO Standard design', 100, 'user', DATE '2011-07-19', 'PENDING', 'PROGRESS', 1),
(111115, 'ECLC52', 'Fevo W EZL customised', 'EZLSTD-EZLINK Standard design', 'EZLINK Standard design', 60, 'user', DATE '2011-07-19', 'PENDING', 'PROGRESS', 1);