
CREATE TRIGGER KreiranjePonude
   ON  Isporuka
   FOR INSERT
AS 
BEGIN
	
	declare @kursor cursor

	declare @IdI integer, @IdAdrOd integer, @IdAdrDo integer, @Tip integer, @Tezina decimal(10,3)

	declare @OsnCena decimal(10,3), @CenaPoKg decimal(10,3), @CenaIsporuke decimal(10,3)

	declare @euklid decimal(10,3)

	declare @Xod integer, @Yod integer, @Xdo integer, @Ydo integer


	set @kursor = cursor for
	select IdI, AdresaOd, AdresaDo, Tip, Tezina
	from inserted

	open @kursor

	fetch from @kursor
	into @IdI, @IdAdrOd, @IdAdrDo, @Tip, @Tezina

	while @@FETCH_STATUS=0
	begin
		
		if(@Tip=0)
		begin
			set @OsnCena=115
			set @CenaPoKg=0
		end
		else if(@Tip=1)
		begin
			set @OsnCena=175
			set @CenaPoKg=100
		end
		else if(@Tip=2)
		begin
			set @OsnCena=250
			set @CenaPoKg=100
		end
		else
		begin
			set @OsnCena=350
			set @CenaPoKg=500
		end

		select @Xod=X, @Yod=Y
		from Adresa
		where IdA=@IdAdrOd

		select @Xdo=X, @Ydo=Y
		from Adresa
		where IdA=@IdAdrDo

		set @euklid=SQRT(POWER(@Xdo-@Xod,2)+POWER(@Ydo-@Yod,2))

		set @CenaIsporuke=(@OsnCena+(@Tezina)*@CenaPoKg)*@euklid

		if(exists(select IdPon from Ponuda where IdPon = @IdI))
		begin
			UPDATE Ponuda set CenaIsporuke = @CenaIsporuke
			where IdPon = @IdI
		end
		else
		begin
			INSERT INTO Ponuda
			values(0, @IdI, @CenaIsporuke)
		end

		fetch from @kursor
		into @IdI, @IdAdrOd, @IdAdrDo, @Tip, @Tezina
	end

	close @kursor
	deallocate @kursor

END
GO
