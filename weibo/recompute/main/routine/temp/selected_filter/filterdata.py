#! /usr/bin/python
import sys
if len(sys.argv)<2:
	print sys.argv[0],"srcfile++"
	sys.exit()
import gzip

def _DocItems(data):
	if data.startswith("@\n@"):
		items=data[3:].split("\n@")
	elif data.startswith("@"):
		items=data[1:].split("\n@")
	else:
		items=data.split("\n@")
	doc={}
	fields=[]
	for item in items:
		pos=item.find(':')
		if pos<0:
			continue
		key=item[:pos]
		doc[key]=item[pos+1:]
		fields.append(key)
	return doc,fields

def CleanText(text):
	content=text.decode("utf8")
	contentlen=len(content)
	texts=[]
	pos=0
	startpos=0
	while pos<contentlen:
		word = content[pos]
		if content[pos]=="<":
			texts.append(content[startpos:pos].encode("utf8"))
			pos1=content.find(">",pos+1)
			if pos1>0:
				pos=pos1+1
			else:
				pos=pos+1
			startpos=pos
		elif content[pos]=="[":
			texts.append(content[startpos:pos].encode("utf8"))
			pos1=content.find("]",pos+1)
			if pos1>0:
				pos=pos1
			else:
				pos=pos+1
			startpos=pos	
		else:
			pos+=1
	texts.append(content[startpos:pos].encode("utf8"))
	return "".join(texts)

def TransData(docdatas):
	docs=docdatas.split("\n@\n@")
	docnum=len(docs)
	doci=0
	endi=docnum-1
	for doc in docs:
		if doci==0:
			if doci==endi:
				doc=doc
			else:
				doc=doc+"\n"
		elif doci==endi:
			doc="@\n@"+doc
		else:
			doc="@\n@"+doc+"\n"
		doci+=1

		items,fields=_DocItems(doc)

		if not items.has_key("ID"):
			continue

		items["ID"]=items["ID"].replace('"','')

		#precheck begin
		if items.has_key("IDXTEXT") and items["IDXTEXT"]:
			print doc
			continue
		#precheck finish
		text=""
		if items.has_key("IDXCONTENT"):
			text=items["IDXCONTENT"]
		if not text:
			text=items.get("CONTENT","")
			text=CleanText(text)
		infos=["@\n"]
		for k in fields:
			v=items[k]
			if v[-1:]=="\n":
				info="".join(["@",k,":",v])
			else:
				info="".join(["@",k,":",v,"\n"])
			infos.append(info)
		info="".join(["@IDXTEXT:",text,"\n"])
		infos.append(info)
		doc="".join(infos)

		print doc

for srcfile in sys.argv[1:]:
	blocklen=500000
	f=open(srcfile,"rb");
	data=f.read(blocklen)
	leftdata=""
	while data:
		dlen=len(data)
		if dlen<blocklen:
			TransData(leftdata+data)
			leftdata=""
			break
		pos=data.rfind("\n@\n@")
		if pos>0:
			pos+=1
			newleftdata=data[pos:]
			data=data[:pos]
			TransData(leftdata+data)
			leftdata=newleftdata
		data=f.read(blocklen)
	
	if leftdata:
		TransData(leftdata)

