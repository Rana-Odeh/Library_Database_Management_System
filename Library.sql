show databases;
drop database Library;
create database Library;
use Library ;
show tables;
create table Section(
Sec_id int NOT NULL ,
Sec_name varchar (30) NOT NULL,
primary key (Sec_id)
);
insert into Section values(1,'Education'),
(2,'Culture'),
(3,'Arts');

create table Publishing_house(
    P_id int NOT NULL ,
    P_name varchar(32),
    P_address varchar(32),
    P_email varchar(32),
    primary key (P_id)  
    );
select * from Publishing_house ;
insert into Publishing_house ( P_id,P_name, P_address , P_email  )
values(1,'Dar al-Massira','Amman,Jordan','sales@massira.jo');
insert into Publishing_house ( P_id,P_name, P_address , P_email  )
values(2,'Dar Al Thaqafa','Amman, Jordan',' info@daralthaqafa.com');
insert into Publishing_house ( P_id,P_name, P_address , P_email  )
values(3,'Al Dar Al Ahlia','Amman,Jordan','alahliabookstore@gmail.com');
insert into Publishing_house ( P_id,P_name, P_address , P_email  )
values(4,'Dar Al-Shorouk',' V6W4+X9W, Ramallah','shorokpr@palnet.com');

create table Author (
    A_id int NOT NULL
    auto_increment ,
    A_name varchar(32) ,
    primary key (A_id)
    );
    insert into Author values(1,'Abbas Mahmoud Al-Akkad'),
(2,'Ghassan Kanafani'),
(3,'Mustafa Mahmoud');
    
    
    create table Subscriber(
    s_id int Not NULL ,
    s_name varchar(30) NOT NULL,
    dataOfBirth varchar(32) , 
    s_ageGroub varchar(30),
    s_email varchar(35), 
    s_address varchar(30),
    s_gender varchar(20),
    primary key(s_id)
    );
insert into Subscriber(s_id, s_name , dataOfBirth, s_ageGroub , s_email ,s_address ,s_gender )
    values(1,'RanaOdeh','10-10-2002','4-12Y','-----','----','female');
insert into Subscriber(s_id, s_name , dataOfBirth, s_ageGroub , s_email ,s_address ,s_gender )
    values(2,'Amani','11-8-2002','19-orOlder','----','----','female');
insert into Subscriber(s_id, s_name , dataOfBirth, s_ageGroub , s_email ,s_address ,s_gender )
    values(3,'Abrar','24-8-2010','13-18Y','---','----','female');
insert into Subscriber(s_id, s_name , dataOfBirth, s_ageGroub , s_email ,s_address ,s_gender )
    values(4,'Ahmad','15-8-2002','19-orOlder','----','---','male');


    create table Sub_Phone (
      s_id int,
	  Phone int,
	 Primary key(s_id,Phone),
     Foreign key (s_id) references Subscriber(s_id)
     ON delete cascade);
     
     insert into Sub_Phone (s_id,Phone)
     values(1,0598122);
     insert into Sub_Phone (s_id,Phone)
     values(1,0597123);
     insert into Sub_Phone (s_id,Phone)
     values(2,0577777);
     insert into Sub_Phone (s_id,Phone)
     values(3,059876);
     insert into Sub_Phone (s_id,Phone)
     values(4,059986);
     
     
create table BooK(
B_id int  NOT NULL ,
Bname  varchar(60) NOT NULL,
Bdescription varchar(30) ,
number_of_pages int NOT NULL,
year_of_issue varchar(30) NOT NULL,
Sec_id int NOT NULL,
A_id int Not NULL,
number_of_Copy int Not Null,
primary key (B_id) ,
foreign key (Sec_id) References Section (Sec_id) 
 ON delete no action
 ON update cascade,
 foreign key (A_id) References Author (A_id)
ON delete no action
ON update cascade );
insert into BooK values(1,'men from the sun','RN',170,'1963',2,2,5),
(2,'Dialogue with my atheist friend','RR',130,'1986',1,3,10),
(3,'Night Diaries','RM',170,'1970',3,3,4),
(4,'Diwan in Literature and Criticism','RN',143,'1990',2,1,8),
(5,'me','RM',165,'1930',3,1,13),
(6,'The little jellyfish','RR',170,'1991',2,2,7);

 create table PH_B_publishes(
    B_id int NOT NULL,
    P_id int NOT NULL,
    Year_Publication varchar(32),
    primary key (P_id,B_id),
    Foreign key (P_id) REFERENCES Publishing_house(P_id)
    ON UPDATE CASCADE 
    ON DELETE NO ACTION,
    Foreign key (B_id) REFERENCES BooK(B_id)
    ON UPDATE CASCADE 
    ON DELETE NO ACTION
    );
    select * from PH_B_publishes ;
    insert into PH_B_publishes values(1,1,'1980');
    insert into PH_B_publishes values(2,3,'2000');
     insert into PH_B_publishes values(3,2,'1877');
    insert into PH_B_publishes values(4,4,'1910');
     insert into PH_B_publishes values(5,1,'1983');
    insert into PH_B_publishes values(6,3,'2001');

create table B_Sub_Borrow(
      s_id int NOT NULL,
      B_id int NOT NULL,
      startDate date ,
	  endDate date,
      numOfBook int,
    primary key (s_id,B_id),
    Foreign key (s_id) REFERENCES Subscriber(s_id)
    ON UPDATE CASCADE 
    ON DELETE NO ACTION,
    Foreign key (B_id) REFERENCES BooK(B_id)
    ON UPDATE CASCADE 
    ON DELETE NO ACTION
    );
 insert into B_Sub_Borrow (s_id, B_id,numOfBook ,startDate  ,endDate  )
    values(1,2,0,'2023-1-2','2023-2-14');
    insert into B_Sub_Borrow (s_id, B_id ,numOfBook ,startDate  ,endDate  )
    values(2,4,0,'2023-2-3','2023-2-17');
    insert into B_Sub_Borrow (s_id, B_id ,numOfBook ,startDate  ,endDate  )
    values(3,5,0,'2023-1-30','2023-2-13');
    insert into B_Sub_Borrow (s_id, B_id ,numOfBook ,startDate  ,endDate  )
    values(4,1,0,'2023-2-2','2023-2-16');
     insert into B_Sub_Borrow (s_id, B_id ,numOfBook ,startDate  ,endDate  )
    values(4,6,0,'2023-2-2','2023-2-16');
    insert into B_Sub_Borrow (s_id, B_id ,numOfBook ,startDate  ,endDate  )
    values(3,3,0,'2023-1-30','2023-2-13');