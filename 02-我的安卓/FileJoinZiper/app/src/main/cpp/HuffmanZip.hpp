#ifndef _HUFFMAN_ZIP_H_
#define _HUFFMAN_ZIP_H_
/*  */
#include<stdio.h>
#include"AlgoTypes32.hpp"
#include<stdlib.h>

/*
普遍使用的压缩算法，
但是，不处理空文件
另外，对于只有几个字符的，还是不建议压缩了，压缩了反而会增加使用空间
*/
class HuffmanZip //: public Object
{
public:
	typedef struct __tree_node
	{
		BYTE_T ch;
		UINT32_T weight;
		struct __tree_node * lchild, *rchild, *parent;
	}TREE_NODE, *PTREE_NODE;
	typedef struct __code_map
	{
		BYTE_T ch;
		INT32_T codeLen;
		BYTE_T code[256];
		//极端情况下，256个字符，可能会导致最长的字符的huff码长度达到255
		//虽然这种可能性基本不可能达到
	}CODE_MAP,*PCODE_MAP;
public:
	HuffmanZip()
	{
		Init();
	}
	virtual ~HuffmanZip()
	{

	}
	BOOL_T ZipFile(char * srcName, char * drtName)
	{
		FILE * sfp = NULL, *dfp = NULL;
		sfp = fopen(srcName, "rb");
		if (sfp == NULL)
		{
			return BOOL_FALSE;
		}


		UINT32_T weights[256] = { 0 };

		BOOL_T rst = getFileByteWeights(sfp, weights);
		if (rst == BOOL_FALSE)
		{
			fclose(sfp);
			return BOOL_TRUE;
		}

		dfp = fopen(drtName, "wb");
		if (dfp == NULL)
		{
			fclose(sfp);
			return BOOL_FALSE;
		}

		INT32_T leafCount = 0;
		TREE_NODE * root = makeHuffmanTree(weights, &leafCount);
		if (root == NULL)//空文件，直接给创建一个空文件就行
		{
			fclose(sfp);
			fclose(dfp);
			return BOOL_TRUE;
		}

		CODE_MAP * codeMap = getCodeMap(root, leafCount);

		rewind(sfp);
		writeHeader(dfp, weights, leafCount);

		BYTE_T buf[256] = { 0 };
		INT32_T m_head = 0;
		INT32_T m_tial = 0;

		BYTE_T ncharr[8] = { 0 };
		BYTE_T temp = 0;
		while (!feof(sfp))
		{
			fread(&temp, sizeof(BYTE_T), 1, sfp);
			if (feof(sfp))
				break;
			for (INT32_T i = 0; i < leafCount; i++)
			{
				if (codeMap[i].ch == temp)
				{
					for (INT32_T j = 0; j < codeMap[i].codeLen; j++)
					{
						m_tial = (m_tial + 1) % 256;
						buf[m_tial] = codeMap[i].code[j];
					}
					break;
				}
			}
			while ((m_tial + 256 - m_head) % 256 >= 8)
			{
				for (INT32_T pi = 0; pi < 8; pi++)
				{
					m_head = (m_head + 1) % 256;
					ncharr[pi] = buf[m_head];
				}
				BYTE_T wch = ByteArray8ToByte(ncharr);
				fwrite(&wch, sizeof(BYTE_T), 1, dfp);
			}
		}

		INT32_T llen = (m_tial + 256 - m_head) % 256;
		if (llen > 0)
		{
			for (INT32_T pi = 0; pi < 8; pi++)
			{
				if (pi < llen)
				{
					m_head = (m_head + 1) % 256;
					ncharr[pi] = buf[m_head];
				}
				else
				{
					ncharr[pi] = '0';
				}

			}
			BYTE_T wch = ByteArray8ToByte(ncharr);
			fwrite(&wch, sizeof(BYTE_T), 1, dfp);
		}
		
		//文件的最后一个字节用来记录文件压缩内容的最后一个字节的可用长度
		//因为这里由于编码长度的原因，最后一个编码长度可能不到8位
		//因此，这里把最后一个编码在字节内的长度记录在文件末尾，这样在解压时，通过最后一个字节获取应该取多长即可
		//如果最后一个字节，正好编码满8位，也就是没有多余的，那么就是最后一个的8位都有效
		//因此此变量就是记录字节内编码的可用长度
		//结构：.... codech lencode eof
		//codech:最后编码字节 lencode:最后编码字节可用编码位数 eof文件结尾标志
		//也就是值在：1-8之间
		BYTE_T leftLen = llen>0?llen:8;
		fwrite(&leftLen, sizeof(BYTE_T), 1, dfp);

		freeTree(root);
		free(codeMap);
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

		UINT32_T weights[256] = { 0 };
		BOOL_T rst = readHeader(sfp, weights);
		if (rst == BOOL_FALSE)
		{
			fclose(sfp);
			return BOOL_FALSE;
		}

		dfp = fopen(drtName, "wb");
		if (dfp == NULL)
		{
			fclose(sfp);
			return BOOL_FALSE;
		}


		INT32_T leafCount = 0;
		TREE_NODE * root = makeHuffmanTree(weights, &leafCount);
		if (root == NULL)//空文件，直接给创建一个空文件就行
		{
			fclose(sfp);
			fclose(dfp);
			return BOOL_TRUE;
		}


		BYTE_T buf[256] = { 0 };
		INT32_T m_head = 0;
		INT32_T m_tial = 0;

		BYTE_T lastch = 0;

		BYTE_T ncharr[8] = { 0 };
		BYTE_T temp = 0;
		TREE_NODE * pnode = root;
		while (!feof(sfp))
		{
			fread(&temp, sizeof(BYTE_T), 1, sfp);
			if (feof(sfp))
				break;
			lastch = temp;

			ByteToByteArray8(temp, ncharr);
			for (INT32_T i = 0; i<8; i++)
			{
				m_tial = (m_tial + 1) % 256;
				buf[m_tial] = ncharr[i];
			}
			//一直保留最后两个字节不进行编码
			//这样的话，等文件读取结束，其中存放的就是最后两个字节的编码
			//那么这16位中，前8位中，其中有一部分是需要编码的，一部分是填充的，不需要编码的
			//那么需要编码的位数就由后8位读取，保存在lastch中的数值
			while ((m_tial + 256 - m_head) % 256 >16)
			{
				m_head = (m_head + 1) % 256;
				BYTE_T cb = buf[m_head];
				if (cb == '0')
				{
					pnode = pnode->lchild;
				}
				else
				{
					pnode = pnode->rchild;
				}

				if (pnode->lchild == NULL && pnode->rchild == NULL)
				{
					BYTE_T wch = pnode->ch;
					fwrite(&wch, sizeof(BYTE_T), 1, dfp);
					pnode = root;
				}

			}
		}
		//压缩的时候已经说了，最后一个字节用来指示，次最后字节参与编码的长度，这个长度就是lastch
		//这样，我们根据需要编码的长度，从编码序列取出对应的位进行编码即可
		for (BYTE_T i = 0; i < lastch; i++)
		{
			m_head = (m_head + 1) % 256;
			BYTE_T cb = buf[m_head];
			if (cb == '0')
			{
				pnode = pnode->lchild;
			}
			else
			{
				pnode = pnode->rchild;
			}

			if (pnode->lchild == NULL && pnode->rchild == NULL)
			{
				BYTE_T wch = pnode->ch;
				fwrite(&wch, sizeof(BYTE_T), 1, dfp);
				pnode = root;
			}
		}
		freeTree(root);
		fclose(sfp);
		fclose(dfp);
		return BOOL_TRUE;
	}
protected:

private:

	void Init()
	{

	}
	BOOL_T getFileByteWeights(FILE * fp, UINT32_T weights[])
	{
		rewind(fp);
		BOOL_T ret = BOOL_FALSE;

		BYTE_T temp = 0;
		while (!feof(fp))            /*8位无符号字符位于0-255之间*/
		{
			fread(&temp, sizeof(BYTE_T), 1, fp);    /*获取对应字符的数量、频数*/
			if (feof(fp))
				break;
			weights[temp]++;
			ret = BOOL_TRUE;
		}
		rewind(fp);

		return ret;
	}
	UINT32_T getTreeNodes(UINT32_T weights[], TREE_NODE * trees[])
	{
		UINT32_T i = 0, trees_size=0;
		while (i < 256)
		{
			if (weights[i] != 0)
			{
				trees[trees_size] = createTreeNode((BYTE_T)i, weights[i]);
				trees_size++;
			}
			i++;
		}
		return trees_size;
	}
	TREE_NODE * makeHuffmanTree(UINT32_T * weights, INT32_T * leafCount)
	{
		TREE_NODE * ret = NULL;
		

		TREE_NODE * trees[256] = { 0 };
		UINT32_T trees_size = getTreeNodes(weights,trees);
		*leafCount = trees_size;
		//针对只有一种字符单独处理，因为这不能避免的可能发生
		if (trees_size == 1)
		{
			TREE_NODE * tpnode = createTreeNode(0, trees[0]->weight);
			tpnode->lchild = trees[0];
			tpnode->rchild =NULL;
			trees[0]->parent = tpnode;
			trees[0] = tpnode;
			return trees[0];
		}
		
		INT32_T fstMinIndex = -1, secMinIndex = -1;
		while (1)
		{
			fstMinIndex = 0;
			secMinIndex = 0;

			for (INT32_T i = 0; i < trees_size; i++)
			{
				if (trees[i]->weight < trees[fstMinIndex]->weight)
				{
					fstMinIndex = i;
				}
			}
			
			if (fstMinIndex == 0)
				secMinIndex = 1;

			for (INT32_T i = 0; i < trees_size; i++)
			{
				//这里，我们跳过了已经找到的最小元素，那么也就是说，在排除最小的元素中，查找最小，也就等价于在所有元素中查找次小
				if (i == fstMinIndex)
				{
					continue;
				}
				if (trees[i]->weight < trees[secMinIndex]->weight)
				{
					secMinIndex = i;
				}
			}

			//合并两个最小树，形成一棵树：fstMin=tpnode=fstMin+secMin
			TREE_NODE * tpnode = createTreeNode(0, trees[fstMinIndex]->weight + trees[secMinIndex]->weight);
			tpnode->lchild = trees[fstMinIndex];
			tpnode->rchild = trees[secMinIndex];
			trees[fstMinIndex]->parent = tpnode;
			trees[secMinIndex]->parent = tpnode;
			trees[fstMinIndex] = tpnode;
			trees[secMinIndex] = NULL;

			//由于我们每次都要遍历整个森林才能获取最小树和次小树，如果每次都遍历256此，显然有点浪费了
			//因为，我们合并书的时候，最小和次小合并到了最小的位置，次小的位置置空了
			//因此，我们把次小位置和最后一个元素进行交换，这样最后一个元素就是空元素，那么我们的总长度就可以进行减小
			//另一个方面就是，我们上面遍历的时候，不用考虑空指针的问题了，少了一个判断，也能增加运行速度哦
			//因为，我们保证了元素长度之内的元素，都是有效元素
			TREE_NODE * pend = trees[trees_size - 1];
			trees[trees_size - 1] = trees[secMinIndex];
			trees[secMinIndex] = pend;
			trees_size--;

			//如果仅仅剩余一颗树了，那就是最终结果了
			//只剩一棵树，绝对在0下标，因为我们前面有交换，如果你直接使用下标为fstMinIndex的，
			//那么可能会由于上面的交换，导致fstMinIndex下标已经被置空，导致错误的返回值
			if (trees_size == 1)
			{
				ret = trees[0];
				break;
			}
		}
		
		return ret;
	}
	void getCodeMapNext(TREE_NODE * tree, CODE_MAP * map, INT32_T * curSize)
	{
		if (tree == NULL)
			return;
		INT32_T psize = *curSize;
		//叶子节点
		if (tree->lchild == NULL && tree->rchild == NULL)
		{
			//取得huff编码，但是是逆序的
			map[psize].ch = tree->ch;
			map[psize].codeLen = 0;
			map[psize].code[0] = 0;
			TREE_NODE * tp = tree;
			while (tp)
			{
				if (tp->parent)
				{
					if (tp == tp->parent->lchild)
					{
						map[psize].code[map[psize].codeLen++] = '0';
					}
					else
					{
						map[psize].code[map[psize].codeLen++] = '1';
					}
				}
				tp = tp->parent;
			}
			map[psize].code[map[psize].codeLen] = 0;
			//做一个逆序，恢复正序
			for (INT32_T i = 0; i < map[psize].codeLen / 2; i++)
			{
				BYTE_T pch = map[psize].code[i];
				map[psize].code[i] = map[psize].code[map[psize].codeLen - 1 - i];
				map[psize].code[map[psize].codeLen - 1 - i] = pch;
			}
			psize++;
		}

		*curSize = psize;
		getCodeMapNext(tree->lchild,map,curSize);
		getCodeMapNext(tree->rchild, map, curSize);
	}
	CODE_MAP * getCodeMap(TREE_NODE * root, INT32_T leafCount)
	{
		CODE_MAP * ret = (CODE_MAP *)malloc(sizeof(CODE_MAP)*leafCount);
		INT32_T curSize = 0;
		getCodeMapNext(root, ret, &curSize);
		return ret;
	}
	
	BYTE_T ByteArray8ToByte(BYTE_T * arr)
	{
		BYTE_T temp = 0;
		for (INT32_T i = 0; i<8; i++)
		{
			if (arr[i] == '0')
				temp |= 0 << (7 - i);
			else if (arr[i] == '1')
				temp |= 1 << (7 - i);
		}
		return temp;
	}
	void ByteToByteArray8(BYTE_T ch, BYTE_T * arr)
	{
		BYTE_T temp = ch;
		for (INT32_T i = 0; i<8; i++)
		{
			if (temp&(1 << (7 - i)))
				arr[i] = '1';
			else
				arr[i] = '0';
		}
	}
	BOOL_T writeHeader(FILE * fp,UINT32_T * weights,UINT32_T leafCount)
	{
		BYTE_T flgs[] = { 'H', 'Z', 'P', 0 };

		fwrite(flgs, sizeof(BYTE_T), 3, fp);

		UINT32_T plen = leafCount;
		AlgoHost2BigEndian(&plen, sizeof(UINT32_T));
		fwrite(&plen, sizeof(UINT32_T), 1, fp);

		UINT32_T i = 0;
		while (i < 256)
		{
			if (weights[i] != 0)
			{
				BYTE_T bt = i;
				UINT32_T bw = weights[i];
				
				fwrite(&bt, sizeof(BYTE_T), 1, fp);
				AlgoHost2BigEndian(&bw, sizeof(UINT32_T));
				fwrite(&bw, sizeof(UINT32_T), 1, fp);
			}
			i++;
		}
		return BOOL_TRUE;
	}
	BOOL_T readHeader(FILE* fp, UINT32_T * weights)
	{
		BYTE_T flgs[] = { 'H', 'Z', 'P', 0 };
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

		UINT32_T plen = 0;
		fread(&plen, sizeof(UINT32_T), 1, fp);
		AlgoBigEndian2Host(&plen, sizeof(UINT32_T));

		BYTE_T bt = 0;
		UINT32_T bw = 0;
		for (UINT32_T pi = 0; pi < plen; pi++)
		{
			fread(&bt, sizeof(BYTE_T), 1, fp);
			fread(&bw, sizeof(UINT32_T), 1, fp);
			AlgoBigEndian2Host(&bw, sizeof(UINT32_T));
			weights[bt] = bw;
		}

		return BOOL_TRUE;
	}
	TREE_NODE * createTreeNode(BYTE_T ch,UINT32_T weight)
	{
		TREE_NODE * ret = (TREE_NODE*)malloc(sizeof(TREE_NODE));
		ret->ch = ch;
		ret->weight = weight;
		ret->lchild = NULL;
		ret->rchild = NULL;
		ret->parent = NULL;
		return ret;
	}
	void freeTree(TREE_NODE * root)
	{
		if (root == NULL)
			return;
		TREE_NODE * lchild = root->lchild;
		TREE_NODE * rchild = root->rchild;

		free(root);

		freeTree(lchild);
		freeTree(rchild);
	}
};


#endif // _HUFFMAN_ZIP_H_