%% @doc Erlang mini project.
-module(add).
-export([start/3, start/4, makeList/1, makeList/2]).

-include_lib("eunit/include/eunit.hrl").

%% @doc TODO: add documentation
-spec start(A,B,Base) -> ok when 
      A::integer(),
      B::integer(), 
      Base::integer().

start(A,B, Base) ->
    start(A, B, Base, [1]).

%% @doc TODO: add documentation
-spec start(A,B,Base, Options) -> ok when 
      A::integer(),
      B::integer(), 
      Base::integer(),
      Option::atom() | tuple(),
      Options::[Option].

start(A,B,Base, Options) ->
   tbi.


%% @doc extends the shortest list with zeros.
-spec fill({A,B}) -> ok when
A::[integer()],
B::[integer()].

fill({A,B}) -> 
MaxL = max(length(A),length(B)),
{fillaux(MaxL,A),fillaux(MaxL,B)}.


%% Help function for fill
-spec fillaux(M,L) -> ok when
M::integer(),
L::[integer()].

fillaux(M,L) ->
case length(L) of
M -> L;
_ -> fillaux(M, [0|L])
end.

%% @doc transforms I into a int list.
-spec makeList(I) -> ok when
  I::integer().

makeList(I) -> makeList(I,[]).

-spec makeList(I, L) -> ok when
  I::integer(),
  L::[integer()].

makeList(I,L) ->
case I of
  0 -> L;
  _ -> R = I rem 10,
  List = [R|L],
  NewI = (I-R) div 10,
  makeList(NewI,List)
  end.



%%=====================================================================
%%    TEST CASES!
%%=====================================================================

  makeList_test_() ->
  [?_assertEqual([1,5,0], makeList(150))],
  [?_assertEqual([1,0,1], makeList(101))].

fill_test_() ->
[?_assertEqual({[1,5],[0,5]}, fill({[1,5],[5]}))],
[?_assertEqual({[],[]}, fill({[],[]}))],
[?_assertEqual({[1,5],[2,5]}, fill({[1,5],[2,5]}))].