
DROP TABLE [Administrator]
go

DROP TABLE [Zahtev]
go

DROP TABLE [Vozena]
go

DROP TABLE [Ponuda]
go

DROP TABLE [IsporucujeSe]
go

DROP TABLE [TrenutnoVozi]
go

DROP TABLE [Kurir]
go

DROP TABLE [Isporuka]
go

DROP TABLE [Korisnik]
go

DROP TABLE [Parkirano]
go

DROP TABLE [Vozilo]
go

DROP TABLE [Magacin]
go

DROP TABLE [Adresa]
go

DROP TABLE [Grad]
go

CREATE TABLE [Administrator]
( 
	[IdA]                integer  NOT NULL 
)
go

CREATE TABLE [Adresa]
( 
	[IdA]                integer  IDENTITY  NOT NULL ,
	[Ulica]              varchar(100)  NOT NULL ,
	[Broj]               integer  NOT NULL ,
	[X]                  integer  NOT NULL ,
	[Y]                  integer  NOT NULL ,
	[IdG]                integer  NOT NULL 
)
go

CREATE TABLE [Grad]
( 
	[IdG]                integer  IDENTITY  NOT NULL ,
	[Naziv]              char(100)  NOT NULL ,
	[PostBr]             char(100)  NOT NULL 
)
go

CREATE TABLE [IsporucujeSe]
( 
	[IdI]                integer  NOT NULL ,
	[IdV]                integer  NOT NULL ,
	[IdKur]              integer  NOT NULL ,
	[StatusPaketa]       integer  NOT NULL 
)
go

CREATE TABLE [Isporuka]
( 
	[IdI]                integer  IDENTITY  NOT NULL ,
	[AdresaOd]           integer  NOT NULL ,
	[AdresaDo]           integer  NOT NULL ,
	[StatusIsporuke]     varchar(20)  NOT NULL ,
	[VremeKreiranja]     datetime  NOT NULL ,
	[VremePrihvatanja]   datetime  NULL ,
	[AdresaTrenutna]     integer  NOT NULL ,
	[IdK]                integer  NOT NULL ,
	[Tezina]             decimal(10,3)  NOT NULL ,
	[Tip]                integer  NOT NULL ,
	[Cena]               decimal(10,3)  NOT NULL 
)
go

CREATE TABLE [Korisnik]
( 
	[IdK]                integer  IDENTITY  NOT NULL ,
	[Ime]                varchar(100)  NOT NULL ,
	[Prezime]            varchar(100)  NOT NULL ,
	[Sifra]              varchar(100)  NOT NULL ,
	[IdA]                integer  NOT NULL ,
	[KorIme]             varchar(100)  NOT NULL 
)
go

CREATE TABLE [Kurir]
( 
	[IdKur]              integer  NOT NULL ,
	[BrIsporPaketa]      integer  NOT NULL ,
	[Status]             integer  NOT NULL ,
	[Profit]             decimal(10,3)  NOT NULL ,
	[VozackaDozvola]     varchar(100)  NULL 
)
go

CREATE TABLE [Magacin]
( 
	[IdM]                integer  IDENTITY  NOT NULL ,
	[IdA]                integer  NOT NULL 
)
go

CREATE TABLE [Parkirano]
( 
	[IdM]                integer  NOT NULL ,
	[IdV]                integer  NOT NULL 
)
go

CREATE TABLE [Ponuda]
( 
	[Status]             integer  NOT NULL ,
	[IdPon]              integer  NOT NULL ,
	[CenaIsporuke]       decimal(10,3)  NOT NULL 
)
go

CREATE TABLE [TrenutnoVozi]
( 
	[IdV]                integer  NOT NULL ,
	[IdKur]              integer  NOT NULL ,
	[TrAdr]              integer  NULL ,
	[PredjeniPut]        decimal(10,3)  NULL ,
	[Kilaza]             decimal(10,3)  NULL 
)
go

CREATE TABLE [Vozena]
( 
	[IdV]                integer  NOT NULL ,
	[IdKur]              integer  NOT NULL 
)
go

CREATE TABLE [Vozilo]
( 
	[IdV]                integer  IDENTITY  NOT NULL ,
	[TipGoriva]          integer  NULL ,
	[Potrosnja]          decimal(10,3)  NOT NULL ,
	[Nosivost]           decimal(10,3)  NOT NULL ,
	[RB]                 varchar(100)  NOT NULL 
)
go

CREATE TABLE [Zahtev]
( 
	[IdZ]                integer  NOT NULL ,
	[VozackaDozvola]     varchar(100)  NOT NULL 
)
go

ALTER TABLE [Administrator]
	ADD CONSTRAINT [XPKAdministrator] PRIMARY KEY  CLUSTERED ([IdA] ASC)
go

ALTER TABLE [Adresa]
	ADD CONSTRAINT [XPKAdresa] PRIMARY KEY  CLUSTERED ([IdA] ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XPKGrad] PRIMARY KEY  CLUSTERED ([IdG] ASC)
go

ALTER TABLE [IsporucujeSe]
	ADD CONSTRAINT [XPKIsporucujeSe] PRIMARY KEY  CLUSTERED ([IdI] ASC,[IdV] ASC,[IdKur] ASC)
go

ALTER TABLE [Isporuka]
	ADD CONSTRAINT [XPKIsporuka] PRIMARY KEY  CLUSTERED ([IdI] ASC)
go

ALTER TABLE [Korisnik]
	ADD CONSTRAINT [XPKKorisnik] PRIMARY KEY  CLUSTERED ([IdK] ASC)
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [XPKKurir] PRIMARY KEY  CLUSTERED ([IdKur] ASC)
go

ALTER TABLE [Magacin]
	ADD CONSTRAINT [XPKMagacin] PRIMARY KEY  CLUSTERED ([IdM] ASC)
go

ALTER TABLE [Parkirano]
	ADD CONSTRAINT [XPKParkirano] PRIMARY KEY  CLUSTERED ([IdM] ASC,[IdV] ASC)
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [XPKPonuda] PRIMARY KEY  CLUSTERED ([IdPon] ASC)
go

ALTER TABLE [TrenutnoVozi]
	ADD CONSTRAINT [XPKTrenutnoVozi] PRIMARY KEY  CLUSTERED ([IdV] ASC,[IdKur] ASC)
go

ALTER TABLE [Vozena]
	ADD CONSTRAINT [XPKVozena] PRIMARY KEY  CLUSTERED ([IdV] ASC,[IdKur] ASC)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XPKVozilo] PRIMARY KEY  CLUSTERED ([IdV] ASC)
go

ALTER TABLE [Zahtev]
	ADD CONSTRAINT [XPKZahtev] PRIMARY KEY  CLUSTERED ([IdZ] ASC)
go


ALTER TABLE [Administrator]
	ADD CONSTRAINT [R_4] FOREIGN KEY ([IdA]) REFERENCES [Korisnik]([IdK])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Adresa]
	ADD CONSTRAINT [R_1] FOREIGN KEY ([IdG]) REFERENCES [Grad]([IdG])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [IsporucujeSe]
	ADD CONSTRAINT [R_28] FOREIGN KEY ([IdI]) REFERENCES [Isporuka]([IdI])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [IsporucujeSe]
	ADD CONSTRAINT [R_37] FOREIGN KEY ([IdV],[IdKur]) REFERENCES [TrenutnoVozi]([IdV],[IdKur])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Isporuka]
	ADD CONSTRAINT [R_19] FOREIGN KEY ([AdresaOd]) REFERENCES [Adresa]([IdA])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Isporuka]
	ADD CONSTRAINT [R_20] FOREIGN KEY ([AdresaDo]) REFERENCES [Adresa]([IdA])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Isporuka]
	ADD CONSTRAINT [R_31] FOREIGN KEY ([AdresaTrenutna]) REFERENCES [Adresa]([IdA])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Isporuka]
	ADD CONSTRAINT [R_33] FOREIGN KEY ([IdK]) REFERENCES [Korisnik]([IdK])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Korisnik]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([IdA]) REFERENCES [Adresa]([IdA])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_6] FOREIGN KEY ([IdKur]) REFERENCES [Korisnik]([IdK])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Magacin]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([IdA]) REFERENCES [Adresa]([IdA])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Parkirano]
	ADD CONSTRAINT [R_29] FOREIGN KEY ([IdM]) REFERENCES [Magacin]([IdM])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Parkirano]
	ADD CONSTRAINT [R_30] FOREIGN KEY ([IdV]) REFERENCES [Vozilo]([IdV])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_23] FOREIGN KEY ([IdPon]) REFERENCES [Isporuka]([IdI])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [TrenutnoVozi]
	ADD CONSTRAINT [R_35] FOREIGN KEY ([IdV]) REFERENCES [Vozilo]([IdV])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [TrenutnoVozi]
	ADD CONSTRAINT [R_36] FOREIGN KEY ([IdKur]) REFERENCES [Kurir]([IdKur])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [TrenutnoVozi]
	ADD CONSTRAINT [R_39] FOREIGN KEY ([TrAdr]) REFERENCES [Adresa]([IdA])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Vozena]
	ADD CONSTRAINT [R_17] FOREIGN KEY ([IdV]) REFERENCES [Vozilo]([IdV])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Vozena]
	ADD CONSTRAINT [R_18] FOREIGN KEY ([IdKur]) REFERENCES [Kurir]([IdKur])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Zahtev]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([IdZ]) REFERENCES [Korisnik]([IdK])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go
