#(set! paper-alist (cons '("my size" . (cons (* 60 cm) (* 10 cm))) paper-alist))
#(set-default-paper-size "my size")

  % Notes on Rhythmic staff
  \new RhythmicStaff {
    \time 32/1
    \autoBeamOff
    c1 c1. c2 c2. c4 c4. c8 c8. c16 c16. c32 c32.
    s16
    r1 r1. r2 r2. r4 r4. r8 r8. r16 r16. r32 r32.
    s16
    \times 2/3 {c1}
    \times 2/3 {c2}
    \times 2/3 {c4}
    \times 2/3 {c8}
    \times 2/3 {c16}
    \times 2/3 {c32}
    s16
    \times 2/3 {r1}
    \times 2/3 {r2}
    \times 2/3 {r4}
    \times 2/3 {r8}
    \times 2/3 {r16}
    \times 2/3 {r32}
    s16
    c8 ~ c8
    s16
        \numericTimeSignature
    \time 2/2 s8
    \time 2/4 s8
    \time 3/4 s8
    \time 4/4 s8
    \time 5/4 s8
    \time 6/4 s8
    \time 7/4 s8
    \time 8/4 s8
    \time 9/4 s8
    \time 10/4 s8
    \time 11/4 s8
    \time 12/4 s8
    \time 2/8 s8
    \time 3/8 s8
    \time 4/8 s8
    \time 5/8 s8
    \time 6/8 s8
    \time 7/8 s8
    \time 8/8 s8
    \time 9/8 s8
    \time 10/8 s8
    \time 11/8 s8
    \time 12/8 s1 s2
  }
  
    % Notes on Rhythmic staff
  \new RhythmicStaff {
    \stopStaff
    \time 32/1
    \autoBeamOff
    c1 c1. c2 c2. c4 c4. c8 c8. c16 c16. c32 c32.
    s16
    r1 r1. r2 r2. r4 r4. r8 r8. r16 r16. r32 r32.
    s16
    \times 2/3 {c1}
    \times 2/3 {c2}
    \times 2/3 {c4}
    \times 2/3 {c8}
    \times 2/3 {c16}
    \times 2/3 {c32}
    s16
    \times 2/3 {r1}
    \times 2/3 {r2}
    \times 2/3 {r4}
    \times 2/3 {r8}
    \times 2/3 {r16}
    \times 2/3 {r32}
    s16
    c8 ~ c8
    s16
        \numericTimeSignature
    \time 2/2 s8
    \time 2/4 s8
    \time 3/4 s8
    \time 4/4 s8
    \time 5/4 s8
    \time 6/4 s8
    \time 7/4 s8
    \time 8/4 s8
    \time 9/4 s8
    \time 10/4 s8
    \time 11/4 s8
    \time 12/4 s8
    \time 2/8 s8
    \time 3/8 s8
    \time 4/8 s8
    \time 5/8 s8
    \time 6/8 s8
    \time 7/8 s8
    \time 8/8 s8
    \time 9/8 s8
    \time 10/8 s8
    \time 11/8 s8
    \time 12/8 s1 s2
  }

