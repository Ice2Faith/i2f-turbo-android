#ifndef _MULTI_FILE_ZIP_H_
#define _MULTI_FILE_ZIP_H_
/*  */



#include"AlgoTypes32.hpp"
#include"FileJoin.hpp"
#include"HuffmanZip.hpp"

class MultiFileZip //: public Object
{
public:
	MultiFileZip()
	{
		Init();
	}
	BOOL_T ZipFiles(INT32_T count, char * srcFiles[], char * drtFile)
	{
		char tpfn[2048] = { 0 };
		char tptail[] = {".tmpfjs"};
		INT32_T i = 0,j=0;
		while (drtFile[i] != 0)
		{
			tpfn[i] = drtFile[i];
			i++;
		}
		while (tptail[j] != 0)
		{
			tpfn[i] = tptail[j];
			j++;
			i++;
		}
		tpfn[i] = 0;

		if (BOOL_TRUE == joiner.join(count, srcFiles, tpfn))
		{
			BOOL_T ret=ziper.ZipFile(tpfn, drtFile);
			remove(tpfn);
			return ret;
		}
		return BOOL_FALSE;
	}
	BOOL_T UnzipFiles(char * srcFile, char * drtPath)
	{
		char tpfn[2048] = { 0 };
		char tptail[] = { ".tmpfjs" };
		INT32_T i = 0, j = 0;
		while (srcFile[i] != 0)
		{
			tpfn[i] = srcFile[i];
			i++;
		}
		while (tptail[j] != 0)
		{
			tpfn[i] = tptail[j];
			j++;
			i++;
		}
		tpfn[i] = 0;

		if (BOOL_TRUE == ziper.UnzipFile(srcFile, tpfn))
		{
			BOOL_T ret= joiner.split(tpfn, drtPath);
			remove(tpfn);
			return ret;
		}
		return BOOL_FALSE;
	}
	virtual ~MultiFileZip()
	{

	}
protected:

private:
	FileJoin joiner;
	HuffmanZip ziper;
	void Init()
	{

	}


};


#endif // _MULTI_FILE_ZIP_H_