#ifndef _FILE_JOIN_H_
#define _FILE_JOIN_H_
/*  */
#include"AlgoTypes32.hpp"
#include<stdio.h>
#include<stdlib.h>

/*
����������Ķ��壬�������ڿ�ƽ̨ʹ����
�����ļ��ĺϲ��ͷָ�漰���ļ����Ķ�д����˴˹���һ��ʹ��UTF8��������ļ���д
��Windows�����У����Ļ���һ����ʹ��GBK����
����Linux�У�Android��JNI�������У�ʹ�õ���UTF8����
*/
#define _UTF8_STR_ENV_ 1
//#define _GBK_WIN_STR_ENV_ 2

#if defined _GBK_WIN_STR_ENV_
#include<Windows.h>
#endif

//�ϲ�һ���ļ�����Щ��ֻ�����ļ������ܰ����ļ��У��ļ��н�������
//���ʱ�������ļ����ᱻ��ѹ��ͬһ��Ŀ¼���������Ҫ��֤�ļ������ظ�
//����ظ��ļ������ᱻ���ǣ�ע��
//�ļ����ظ������ͬĿ¼���ظ��ļ�Ҳ�����ظ�
class FileJoin //: public Object
{
protected:
	static char * hostToUtf8(char * hostStr, char * utf8Str)
	{
#if defined _UTF8_STR_ENV_
		INT32_T len=0;
		while (hostStr[len]!=0)
		{
			utf8Str[len]=hostStr[len];
			len++;
		}
		utf8Str[len] = 0;
#elif defined _GBK_WIN_STR_ENV_
		//gbk ����ת utf8���룬��win API�ṩ����
		int size = MultiByteToWideChar(CP_ACP, 0, hostStr, -1, NULL, 0);
		WCHAR * tpUniName = (WCHAR*)malloc((size + 1)*sizeof(WCHAR));
		MultiByteToWideChar(CP_ACP, 0, hostStr, -1, tpUniName, size);

		size = WideCharToMultiByte(CP_UTF8, 0, tpUniName, -1, NULL, 0, NULL, NULL);
		WideCharToMultiByte(CP_UTF8, 0, tpUniName, -1, utf8Str, size, NULL, NULL);
		free(tpUniName);
#else
#error "��֧�ֵĶ��ֽڱ����ַ�������ǰ��֧��UTF8��GBK����"
#endif
		return utf8Str;
	}
	static char * utf8ToHost(char * utf8Str, char * hostStr)
	{
#if defined _UTF8_STR_ENV_
		INT32_T len = 0;
		while (utf8Str[len] != 0)
		{
			hostStr[len] = utf8Str[len];
			len++;
		}
		hostStr[len] = 0;
#elif defined _GBK_WIN_STR_ENV_
		//utf8 ����ת gbk���룬��win API�ṩ����
		int size = MultiByteToWideChar(CP_UTF8, 0, utf8Str, -1, NULL, 0);
		WCHAR * tpUniName = (WCHAR*)malloc((size + 1)*sizeof(WCHAR));
		MultiByteToWideChar(CP_UTF8, 0, utf8Str, -1, tpUniName, size);

		size = WideCharToMultiByte(CP_ACP, 0, tpUniName, -1, NULL, 0, NULL, NULL);
		WideCharToMultiByte(CP_ACP, 0, tpUniName, -1, hostStr, size, NULL, NULL);
		free(tpUniName);
#else
#error "��֧�ֵĶ��ֽڱ����ַ�������ǰ��֧��UTF8��GBK����"
#endif
		return hostStr;
	}
public:
	FileJoin()
	{
		Init();
	}
	
	//�������ļ����� �ļ�Ŀ¼���� Ŀ���ļ�
	BOOL_T join(INT32_T count,char * srcFiles[], char * drtFile)
	{
		FILE * dfp = NULL, *sfp = NULL;
		for (INT32_T i = 0; i < count; i++)
		{
			sfp = NULL;
			sfp = fopen(srcFiles[i], "rb");
			if (sfp == NULL)
			{
				continue;
			}
			else
			{
				if (dfp == NULL)
				{
					dfp = fopen(drtFile,"wb");
					if (dfp == NULL)
					{
						if (sfp)
						{
							fclose(sfp);
						}
						return BOOL_FALSE;
					}
					writeHeader(dfp);
				}
			}
			fseek(sfp, 0, SEEK_END);
			UINT32_T end = ftell(sfp);
			rewind(sfp);
			UINT32_T begin = ftell(sfp);

			char * pname = getFileNameInPath(srcFiles[i]);

			char name[300] = { 0 };
			hostToUtf8(pname, name);

			UINT16_T name_len = 0;
			while (name[name_len] != 0)
			{
				name_len++;
			}
			UINT16_T wname_len = name_len;
			AlgoHost2BigEndian(&wname_len, sizeof(UINT16_T));
			fwrite(&wname_len, sizeof(UINT16_T), 1, dfp);
			fwrite(name, sizeof(char), name_len, dfp);


			UINT32_T file_len = end - begin;
			AlgoHost2BigEndian(&file_len, sizeof(UINT32_T));
			fwrite(&file_len,sizeof(UINT32_T),1,dfp);
			
			BYTE_T curch = 0;
			while (!feof(sfp))            /*8λ�޷����ַ�λ��0-255֮��*/
			{
				fread(&curch, sizeof(BYTE_T), 1, sfp);    /*��ȡ��Ӧ�ַ���������Ƶ��*/
				if (feof(sfp))
					break;
				fwrite(&curch, sizeof(BYTE_T), 1, dfp);
			}

			fclose(sfp);
		}

		fclose(dfp);
		return BOOL_TRUE;
	}
	BOOL_T split(char * srcFile, char * drtPath)
	{
		FILE * dfp = NULL, *sfp = NULL;
		sfp = fopen(srcFile,"rb");
		if (sfp == NULL)
		{
			return BOOL_FALSE;
		}
		if (readHeader(sfp)==BOOL_FALSE)
		{
			fclose(sfp);
			return BOOL_FALSE;
		}
		
		while (1)
		{
			UINT16_T pname_zie = 0;
			char pname[256] = { 0 };

			fread(&pname_zie, sizeof(UINT16_T), 1, sfp);
			if (feof(sfp))
				break;
			AlgoBigEndian2Host(&pname_zie, sizeof(UINT16_T));
			fread(pname, sizeof(char), pname_zie, sfp);
			pname[pname_zie] = 0;

			char name[300] = { 0 };
			utf8ToHost(pname, name);

			
			char drtFile[2048] = { 0 };
			INT32_T i = 0;
			while (drtPath[i] != 0)
			{
				drtFile[i] = drtPath[i];
				i++;
			}
			INT32_T j = 0;
			while (name[j] != 0)
			{
				drtFile[i] = name[j];
				i++;
				j++;
			}
			drtFile[i] = 0;

			dfp = NULL;
			dfp = fopen(drtFile, "wb");
			if (dfp == NULL)
			{
				fclose(sfp);
				return BOOL_FALSE;
			}

			
			UINT32_T psize = 0, rsize = 0;
			fread(&psize, sizeof(UINT32_T), 1, sfp);
			AlgoBigEndian2Host(&psize, sizeof(UINT32_T));

			BYTE_T temp = 0;
			while (rsize < psize)
			{
				fread(&temp, sizeof(BYTE_T), 1, sfp);
				fwrite(&temp, sizeof(BYTE_T), 1, dfp);
				rsize++;
			}

			fclose(dfp);

		}

		fclose(sfp);
		return BOOL_TRUE;
	}
	virtual ~FileJoin()
	{

	}
protected:

private:

	void Init()
	{

	}
	char * getFileNameInPath(char * path,INT32_T * index=NULL)
	{
		INT32_T i = 0;
		while (path[i] != 0)
		{
			i++;
		}
		i--;
		//ƥ����ӦWindows/Linuxƽ̨
		while (i>0 && path[i] != '/' && path[i] != '\\' && path[i] != ':')
		{
			i--;
		}
		if (i!=0)
			i++;
		if (index != NULL)
			*index = i;
		return &path[i];
	}
	BOOL_T writeHeader(FILE * fp)
	{
		BYTE_T flgs[] = { 'F', 'J', 'S', 0 };
		fwrite(flgs, sizeof(BYTE_T), 3, fp);
		return BOOL_TRUE;
	}
	BOOL_T readHeader(FILE * fp)
	{
		BYTE_T flgs[] = { 'F', 'J', 'S', 0 };
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


#endif // _FILE_JOIN_H_