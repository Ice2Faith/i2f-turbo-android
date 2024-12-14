#ifndef _SIMPLE_ZIP_H_
#define _SIMPLE_ZIP_H_
/*  */
#include<stdio.h>
#include"AlgoTypes32.hpp"
#include<stdlib.h>
/*
适用于字符序列中连续重复度高的文件进行压缩，例如：
文件内容：aaaabbbb
那么将会被压缩为：5a4b
但是如果你的文件内容是：abab
那么就不是压缩了，而是增长了：1a1b1a1b
所以，这种压缩方式是具有针对性的
*/
class SimpleZip //: public Object
{
public:
	SimpleZip()
	{
		Init();
	}
	BOOL_T ZipFile(char * srcName, char * drtName)
	{
		FILE * sfp = NULL, *dfp = NULL;
		sfp = fopen(srcName, "rb");
		if (sfp == NULL)
		{
			return BOOL_FALSE;
		}
		dfp = fopen(drtName, "wb");
		if (dfp == NULL)
		{
			fclose(sfp);
			return BOOL_FALSE;
		}
		writeHeader(dfp);

		BYTE_T curch = 0, lastch = 0;
		BYTE_T count = 0;
		while (!feof(sfp))            /*8位无符号字符位于0-255之间*/
		{
			fread(&curch, sizeof(BYTE_T), 1, sfp);    /*获取对应字符的数量、频数*/
			if (feof(sfp))
				break;
			if (count == 0)
			{
				lastch = curch;
			}
			if (curch == lastch)
			{
				count++;
				if (count == 255)
				{
					fwrite(&count, sizeof(BYTE_T), 1, dfp);
					fwrite(&curch, sizeof(BYTE_T), 1, dfp);
				}
			}
			else
			{
				fwrite(&count, sizeof(BYTE_T), 1, dfp);
				fwrite(&lastch, sizeof(BYTE_T), 1, dfp);
				count = 1;
				lastch = curch;
			}
		}
		fwrite(&count, sizeof(BYTE_T), 1, dfp);
		fwrite(&curch, sizeof(BYTE_T), 1, dfp);

		fclose(sfp);
		fclose(dfp);
		return BOOL_TRUE;
	}
	BOOL_T UnzipFile(char * srcName, char * drtName)
	{
		FILE * sfp = NULL, *dfp = NULL;
		sfp = fopen(srcName, "rb");
		if (sfp == NULL)
		{
			return BOOL_FALSE;
		}
		dfp = fopen(drtName, "wb");
		if (dfp == NULL)
		{
			fclose(sfp);
			return BOOL_FALSE;
		}
		if (readHeader(sfp) == BOOL_FALSE)
		{
			fclose(sfp);
			fclose(dfp);
			return BOOL_FALSE;
		}

		BYTE_T curch = 0;
		BYTE_T count = 0;
		while (!feof(sfp))            /*8位无符号字符位于0-255之间*/
		{
			fread(&count, sizeof(BYTE_T), 1, sfp);
			fread(&curch, sizeof(BYTE_T), 1, sfp);    /*获取对应字符的数量、频数*/
			if (feof(sfp))
				break;
			for (BYTE_T i = 0; i < count; i++)
			{
				fwrite(&curch, sizeof(BYTE_T), 1, dfp);
			}
		}

		fclose(sfp);
		fclose(dfp);
		return BOOL_TRUE;
	}
	virtual ~SimpleZip()
	{

	}
protected:

private:

	void Init()
	{

	}
	BOOL_T writeHeader(FILE * fp)
	{
		BYTE_T flgs[] = { 'S', 'Z', 'P', 0 };
		fwrite(flgs, sizeof(BYTE_T), 3, fp);
		return BOOL_TRUE;
	}
	BOOL_T readHeader(FILE * fp)
	{
		BYTE_T flgs[] = { 'S', 'Z', 'P', 0 };
		BYTE_T rflg[8] = { 0 };
		fread(rflg, sizeof(BYTE_T), 3, fp);
		INT32_T i = 0;
		while (flgs[i] != 0)
		{
			if (flgs[i] != rflg[i])
			{
				return BOOL_FALSE;
			}
			i++;
		}
		return BOOL_TRUE;
	}

};


#endif // _SIMPLE_ZIP_H_
