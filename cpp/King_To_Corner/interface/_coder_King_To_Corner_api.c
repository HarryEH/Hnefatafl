/*
 * Academic License - for use in teaching, academic research, and meeting
 * course requirements at degree granting institutions only.  Not for
 * government, commercial, or other organizational use.
 * File: _coder_King_To_Corner_api.c
 *
 * MATLAB Coder version            : 3.3
 * C/C++ source code generated on  : 15-May-2017 17:02:19
 */

/* Include Files */
#include "tmwtypes.h"
#include "_coder_King_To_Corner_api.h"
#include "_coder_King_To_Corner_mex.h"

/* Variable Definitions */
emlrtCTX emlrtRootTLSGlobal = NULL;
emlrtContext emlrtContextGlobal = { true,/* bFirstTime */
  false,                               /* bInitialized */
  131450U,                             /* fVersionInfo */
  NULL,                                /* fErrorFunction */
  "King_To_Corner",                    /* fFunctionName */
  NULL,                                /* fRTCallStack */
  false,                               /* bDebugMode */
  { 2045744189U, 2170104910U, 2743257031U, 4284093946U },/* fSigWrd */
  NULL                                 /* fSigMem */
};

/* Function Declarations */
static int8_T (*b_emlrt_marshallIn(const mxArray *u, const emlrtMsgIdentifier
  *parentId))[121];
static int8_T c_emlrt_marshallIn(const mxArray *x, const char_T *identifier);
static int8_T d_emlrt_marshallIn(const mxArray *u, const emlrtMsgIdentifier
  *parentId);
static int8_T (*e_emlrt_marshallIn(const mxArray *src, const emlrtMsgIdentifier *
  msgId))[121];
static int8_T (*emlrt_marshallIn(const mxArray *board, const char_T *identifier))
  [121];
static const mxArray *emlrt_marshallOut(const int8_T u[4]);
static int8_T f_emlrt_marshallIn(const mxArray *src, const emlrtMsgIdentifier
  *msgId);

/* Function Definitions */

/*
 * Arguments    : const mxArray *u
 *                const emlrtMsgIdentifier *parentId
 * Return Type  : int8_T (*)[121]
 */
static int8_T (*b_emlrt_marshallIn(const mxArray *u, const emlrtMsgIdentifier
  *parentId))[121]
{
  int8_T (*y)[121];
  y = e_emlrt_marshallIn(emlrtAlias(u), parentId);
  emlrtDestroyArray(&u);
  return y;
}
/*
 * Arguments    : const mxArray *x
 *                const char_T *identifier
 * Return Type  : int8_T
 */
  static int8_T c_emlrt_marshallIn(const mxArray *x, const char_T *identifier)
{
  int8_T y;
  emlrtMsgIdentifier thisId;
  thisId.fIdentifier = (const char *)identifier;
  thisId.fParent = NULL;
  thisId.bParentIsCell = false;
  y = d_emlrt_marshallIn(emlrtAlias(x), &thisId);
  emlrtDestroyArray(&x);
  return y;
}

/*
 * Arguments    : const mxArray *u
 *                const emlrtMsgIdentifier *parentId
 * Return Type  : int8_T
 */
static int8_T d_emlrt_marshallIn(const mxArray *u, const emlrtMsgIdentifier
  *parentId)
{
  int8_T y;
  y = f_emlrt_marshallIn(emlrtAlias(u), parentId);
  emlrtDestroyArray(&u);
  return y;
}

/*
 * Arguments    : const mxArray *src
 *                const emlrtMsgIdentifier *msgId
 * Return Type  : int8_T (*)[121]
 */
static int8_T (*e_emlrt_marshallIn(const mxArray *src, const emlrtMsgIdentifier *
  msgId))[121]
{
  int8_T (*ret)[121];
  static const int32_T dims[2] = { 11, 11 };

  emlrtCheckBuiltInR2012b(emlrtRootTLSGlobal, msgId, src, "int8", false, 2U,
    dims);
  ret = (int8_T (*)[121])mxGetData(src);
  emlrtDestroyArray(&src);
  return ret;
}
/*
 * Arguments    : const mxArray *board
 *                const char_T *identifier
 * Return Type  : int8_T (*)[121]
 */
  static int8_T (*emlrt_marshallIn(const mxArray *board, const char_T
  *identifier))[121]
{
  int8_T (*y)[121];
  emlrtMsgIdentifier thisId;
  thisId.fIdentifier = (const char *)identifier;
  thisId.fParent = NULL;
  thisId.bParentIsCell = false;
  y = b_emlrt_marshallIn(emlrtAlias(board), &thisId);
  emlrtDestroyArray(&board);
  return y;
}

/*
 * Arguments    : const int8_T u[4]
 * Return Type  : const mxArray *
 */
static const mxArray *emlrt_marshallOut(const int8_T u[4])
{
  const mxArray *y;
  const mxArray *m0;
  static const int32_T iv0[2] = { 0, 0 };

  static const int32_T iv1[2] = { 1, 4 };

  y = NULL;
  m0 = emlrtCreateNumericArray(2, iv0, mxINT8_CLASS, mxREAL);
  mxSetData((mxArray *)m0, (void *)&u[0]);
  emlrtSetDimensions((mxArray *)m0, *(int32_T (*)[2])&iv1[0], 2);
  emlrtAssign(&y, m0);
  return y;
}

/*
 * Arguments    : const mxArray *src
 *                const emlrtMsgIdentifier *msgId
 * Return Type  : int8_T
 */
static int8_T f_emlrt_marshallIn(const mxArray *src, const emlrtMsgIdentifier
  *msgId)
{
  int8_T ret;
  static const int32_T dims = 0;
  emlrtCheckBuiltInR2012b(emlrtRootTLSGlobal, msgId, src, "int8", false, 0U,
    &dims);
  ret = *(int8_T *)mxGetData(src);
  emlrtDestroyArray(&src);
  return ret;
}

/*
 * Arguments    : const mxArray *prhs[3]
 *                const mxArray *plhs[1]
 * Return Type  : void
 */
void King_To_Corner_api(const mxArray *prhs[3], const mxArray *plhs[1])
{
  int8_T (*move_to_corner)[4];
  int8_T (*board)[121];
  int8_T x;
  int8_T y;
  move_to_corner = (int8_T (*)[4])mxMalloc(sizeof(int8_T [4]));
  prhs[0] = emlrtProtectR2012b(prhs[0], 0, false, -1);

  /* Marshall function inputs */
  board = emlrt_marshallIn(emlrtAlias(prhs[0]), "board");
  x = c_emlrt_marshallIn(emlrtAliasP(prhs[1]), "x");
  y = c_emlrt_marshallIn(emlrtAliasP(prhs[2]), "y");

  /* Invoke the target function */
  King_To_Corner(*board, x, y, *move_to_corner);

  /* Marshall function outputs */
  plhs[0] = emlrt_marshallOut(*move_to_corner);
}

/*
 * Arguments    : void
 * Return Type  : void
 */
void King_To_Corner_atexit(void)
{
  mexFunctionCreateRootTLS();
  emlrtEnterRtStackR2012b(emlrtRootTLSGlobal);
  emlrtLeaveRtStackR2012b(emlrtRootTLSGlobal);
  emlrtDestroyRootTLS(&emlrtRootTLSGlobal);
  King_To_Corner_xil_terminate();
}

/*
 * Arguments    : void
 * Return Type  : void
 */
void King_To_Corner_initialize(void)
{
  mexFunctionCreateRootTLS();
  emlrtClearAllocCountR2012b(emlrtRootTLSGlobal, false, 0U, 0);
  emlrtEnterRtStackR2012b(emlrtRootTLSGlobal);
  emlrtFirstTimeR2012b(emlrtRootTLSGlobal);
}

/*
 * Arguments    : void
 * Return Type  : void
 */
void King_To_Corner_terminate(void)
{
  emlrtLeaveRtStackR2012b(emlrtRootTLSGlobal);
  emlrtDestroyRootTLS(&emlrtRootTLSGlobal);
}

/*
 * File trailer for _coder_King_To_Corner_api.c
 *
 * [EOF]
 */
