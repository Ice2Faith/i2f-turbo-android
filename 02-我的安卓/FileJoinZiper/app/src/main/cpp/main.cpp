#include<stdio.h>
#include"FileJoin.hpp"
#include"HuffmanZip.hpp"
#include"MultiFileZip.hpp"
#include<stdlib.h>
#include<string.h>
int main(int argc, char * argv[])
{
	char helpInfo[] = {"命令行参数：\n\
\t模式 目标路径 源路径1 源路径2 ...\n\
\t模式：\n\
\t\thzp:\t单文件压缩\n\
\t\t\thzp test_save.hzp test.mp3\n\
\t\tdhzp:\t单文件解压\n\
\t\t\tdhzp test.mp3 test_save.hzp\n\
\t\tfjs:\t多文件拼接\n\
\t\t\tfjs test_save.fjs test1.mp3 test2.mp4\n\
\t\tdfjs:\t多文件拆分\n\
\t\t\tdfjs .\savepath\ test_save.fjs\n\
\t\tmzp:\t多文件压缩\n\
\t\t\tmzp test_save.mzp test.png test.mp3 test.txt\n\
\t\tdmzp:\t多文件解压\n\
\t\t\tdmzp .\savepath\ test_save.mzp\n\
\t注意：\n\
\t\t通过以上例子，你应该知道了，多文件拆分和多文件解压时\n\
\t\t目标路径需要是一个文件夹，也就是符号【\\】结尾\n\
\t\t多文件压缩的本质：先合并多文件 再压缩\n\
" };
	if (argc < 4)
	{
		printf(helpInfo);
		system("pause");
		return 0;
	}
	HuffmanZip ziper;

	FileJoin joiner;

	MultiFileZip mulziper;

	char * mode = argv[1];
	char * drtFile = argv[2];
	int beginInputIndex = 3;

	BOOL_T rst = BOOL_FALSE;
	if (strcmp("hzp", mode) == 0)
	{
		rst=ziper.ZipFile(argv[beginInputIndex], drtFile);
	}
	else if (strcmp("dhzp", mode) == 0)
	{
		rst = ziper.UnzipFile(argv[beginInputIndex], drtFile);
	}
	else if (strcmp("fjs", mode) == 0)
	{
		rst = joiner.join(argc - beginInputIndex, &argv[beginInputIndex], drtFile);
	}
	else if (strcmp("dfjs", mode) == 0)
	{
		rst = joiner.split(argv[beginInputIndex], drtFile);
	}
	else if (strcmp("mzp", mode) == 0)
	{
		rst = mulziper.ZipFiles(argc - beginInputIndex, &argv[beginInputIndex], drtFile);
	}
	else if (strcmp("dmzp", mode) == 0)
	{
		rst = mulziper.UnzipFiles(argv[beginInputIndex], drtFile);
	}
	else
	{
		printf("未识别的模式，请注意大小写，以下是使用帮助：");
		printf(helpInfo);
		system("pause");
		return 0;
	}
	if (rst == BOOL_TRUE)
	{
		printf("操作成功,模式：%s\n",mode);
	}
	else
	{
		printf("操作失败，请检查文件或参数是否符合要求\n");
	}

	return 0;
}