# Toolkit_for_automated_reasoning_and_interpolation_with_ordered_resolution

In this bachelor thesis, we developed a toolkit for automated reasoning and interpolation
with ordered resolution. Firstly, the theoretical aspects of propositional logic are covered
in the preliminaries section, including syntax and semantics, the transformation of formulae
in conjunctive and disjunctive normal form, the ordered resolution calculus with
redundancy elimination, and Craig interpolation. Based on this, we developed a console
application which provides these functionalities. The program can be run in a teaching
mode or the result mode and serves as a guideline for students in propositional logic.
The program is written in the programming language Java.

For the implementation of the ordered resolution calculus, we require a formula as an
input. We implemented two methods for computing CNFs, the first one using equivalences
in propositional logic, the second one using structure-preserving transformations.
Through ordered resolution, we test the satisfiability of a clause set. If it is unsatis-
fiable, we return falsum and we construct proof for the unsatisfiability of the clause.
Otherwise, if the clause set is satisfiable, we return a model of the clause set using the
canonical construction of interpretations. The ordered resolution calculus is used in a
further test, to identify redundant clauses of a clause set and eliminate them. For the
Craig interpolation, we use the ordered resolution calculus. Only specified atoms are
used in the inferences of the ordered resolution. These atoms are marked as maximal in
their clause. Last but not least, Craig interpolation is a further application of the ordered
resolution calculus. Craig interpolation returns an interpolant, which is the cause for the
unsatisfiability of the union of two clause sets.

The theoratical concepts and the further documentation of the source code is presented in thesis.pdf.
