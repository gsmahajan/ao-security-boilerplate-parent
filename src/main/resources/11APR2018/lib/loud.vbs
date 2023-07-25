Dim text, sapi

Set file = CreateObject("Scripting.FileSystemObject").OpenTextFile ("d:\loud.txt", 1)

Set sapi=CreateObject("sapi.spvoice")
	sapi.Rate = 2
	sapi.volume = 100

Do Until file.AtEndOfStream
	text= file.Readline
	sapi.Speak Text
Loop