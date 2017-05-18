//
// Academic License - for use in teaching, academic research, and meeting
// course requirements at degree granting institutions only.  Not for
// government, commercial, or other organizational use.
// File: King_To_Corner.cpp
//
// MATLAB Coder version            : 3.3
// C/C++ source code generated on  : 15-May-2017 17:02:19
//

// Include Files
#include "rt_nonfinite.h"
#include "King_To_Corner.h"

// Function Definitions

//
// GENERATE_MOVES_FOR_PIECE Takes the 2d 11x11 array and returns the number
// of moves to each corner tl, tr, bl, br
//    Detailed explanation goes here
// Arguments    : signed char board[121]
//                signed char x
//                signed char y
//                signed char move_to_corner[4]
// Return Type  : void
//
void King_To_Corner(signed char board[121], signed char x, signed char y, signed
                    char move_to_corner[4])
{
  int move_counter;
  int j;
  signed char mtmp;
  signed char maxval[11];
  int i;
  signed char one_depth[40];
  boolean_T left_t;
  boolean_T up_t;
  boolean_T down_t;
  boolean_T right_t;
  int q;
  int b_i;
  for (move_counter = 0; move_counter < 4; move_counter++) {
    move_to_corner[move_counter] = 0;
  }

  //    Not sure if this kind of index checking is appropriate
  //    This should probably be on the java side
  if ((x > 11) || (x < 1) || (y > 11) || (y < 1)) {
  } else {
    for (j = 0; j < 11; j++) {
      maxval[j] = board[11 * j];
      for (i = 0; i < 10; i++) {
        mtmp = maxval[j];
        if (board[(i + 11 * j) + 1] > maxval[j]) {
          mtmp = board[(i + 11 * j) + 1];
        }

        maxval[j] = mtmp;
      }
    }

    mtmp = maxval[0];
    for (move_counter = 0; move_counter < 10; move_counter++) {
      if (maxval[move_counter + 1] > mtmp) {
        mtmp = maxval[move_counter + 1];
      }
    }

    if (mtmp > 0) {
    } else {
      for (j = 0; j < 11; j++) {
        maxval[j] = board[11 * j];
        for (i = 0; i < 10; i++) {
          mtmp = maxval[j];
          if (board[(i + 11 * j) + 1] < maxval[j]) {
            mtmp = board[(i + 11 * j) + 1];
          }

          maxval[j] = mtmp;
        }
      }

      mtmp = maxval[0];
      for (move_counter = 0; move_counter < 10; move_counter++) {
        if (maxval[move_counter + 1] < mtmp) {
          mtmp = maxval[move_counter + 1];
        }
      }

      if ((mtmp < -1) || ((board[x + 11 * (y - 1)] == -1) && (board[(x + 11 * y)
            - 1] == -1) && (board[(x + 11 * (y - 1)) - 2] == -1) && (board[(x +
             11 * (y - 2)) - 1] == -1))) {
      } else {
        //   Corner indexes are 1,1 & 1,11 & 11,11 & 11,1
        //      Check if the piece x,y can move at all
        //      We only continue if the piece can move
        for (move_counter = 0; move_counter < 40; move_counter++) {
          one_depth[move_counter] = 0;
        }

        if (board[(x + 11 * (y - 1)) - 1] != 0) {
          left_t = true;
          up_t = true;
          down_t = true;
          right_t = true;
          move_counter = 0;
          i = 1;
          while ((i - 1 < 10) && (left_t || right_t || up_t || down_t)) {
            if (left_t) {
              if ((x - i > 0) && (board[((x - i) + 11 * (y - 1)) - 1] != -1)) {
                one_depth[move_counter] = (signed char)(x - i);
                one_depth[20 + move_counter] = y;
                move_counter++;
              } else {
                left_t = false;
              }
            }

            if (right_t) {
              if ((x + i < 12) && (board[x + 11 * (y - 1)] != -1)) {
                one_depth[move_counter] = (signed char)(x + i);
                one_depth[20 + move_counter] = y;
                move_counter++;
              } else {
                right_t = false;
              }
            }

            if (up_t) {
              if ((y + i < 12) && (board[(x + 11 * ((y + i) - 1)) - 1] != -1)) {
                one_depth[move_counter] = x;
                one_depth[20 + move_counter] = (signed char)(y + i);
                move_counter++;
              } else {
                up_t = false;
              }
            }

            if (down_t) {
              if ((y - i > 0) && (board[(x + 11 * ((y - i) - 1)) - 1] != -1)) {
                one_depth[move_counter] = x;
                one_depth[20 + move_counter] = (signed char)(y - i);
                move_counter++;
              } else {
                down_t = false;
              }
            }

            i++;
          }
        }

        i = 0;
        while ((i < 20) && (one_depth[i] > 0) && (one_depth[20 + i] > 0)) {
          board[(one_depth[i] + 11 * (one_depth[20 + i] - 1)) - 1] = 1;
          i++;
        }

        for (q = 0; q < 5; q++) {
          for (i = 0; i < 11; i++) {
            for (j = 0; j < 11; j++) {
              if (board[i + 11 * j] == q + 1) {
                for (move_counter = 0; move_counter < 40; move_counter++) {
                  one_depth[move_counter] = 0;
                }

                if (board[i + 11 * j] != 0) {
                  left_t = true;
                  up_t = true;
                  down_t = true;
                  right_t = true;
                  move_counter = 0;
                  b_i = 0;
                  while ((b_i < 10) && (left_t || right_t || up_t || down_t)) {
                    if (left_t) {
                      if ((i - b_i > 0) && (board[((i - b_i) + 11 * j) - 1] !=
                                            -1)) {
                        one_depth[move_counter] = (signed char)(i - b_i);
                        one_depth[20 + move_counter] = (signed char)(1 + j);
                        move_counter++;
                      } else {
                        left_t = false;
                      }
                    }

                    if (right_t) {
                      if (((i + b_i) + 2 < 12) && (board[(i + 11 * j) + 1] != -1))
                      {
                        one_depth[move_counter] = (signed char)((i + b_i) + 2);
                        one_depth[20 + move_counter] = (signed char)(1 + j);
                        move_counter++;
                      } else {
                        right_t = false;
                      }
                    }

                    if (up_t) {
                      if (((j + b_i) + 2 < 12) && (board[i + 11 * ((j + b_i) + 1)]
                           != -1)) {
                        one_depth[move_counter] = (signed char)(1 + i);
                        one_depth[20 + move_counter] = (signed char)((j + b_i) +
                          2);
                        move_counter++;
                      } else {
                        up_t = false;
                      }
                    }

                    if (down_t) {
                      if ((j - b_i > 0) && (board[i + 11 * ((j - b_i) - 1)] !=
                                            -1)) {
                        one_depth[move_counter] = (signed char)(1 + i);
                        one_depth[20 + move_counter] = (signed char)(j - b_i);
                        move_counter++;
                      } else {
                        down_t = false;
                      }
                    }

                    b_i++;
                  }
                }

                move_counter = 0;
                while ((move_counter < 20) && (one_depth[move_counter] > 0) &&
                       (one_depth[20 + move_counter] > 0)) {
                  if (board[(one_depth[move_counter] + 11 * (one_depth[20 +
                        move_counter] - 1)) - 1] == 0) {
                    board[(one_depth[move_counter] + 11 * (one_depth[20 +
                            move_counter] - 1)) - 1] = (signed char)(2 + q);
                  }

                  move_counter++;
                }
              }
            }
          }
        }

        move_to_corner[0] = board[0];
        move_to_corner[1] = board[110];
        move_to_corner[2] = board[10];
        move_to_corner[3] = board[120];
      }
    }
  }
}

//
// File trailer for King_To_Corner.cpp
//
// [EOF]
//
