%% @doc Erlang mini project.
-module(add).
-export([start/3, start/4, makeList/1, makeList/2,fillaux/2, split/2]).

-include_lib("eunit/include/eunit.hrl").

%% @doc TODO: add documentation
-spec start(A,B,Base) -> ok when 
      A::integer(),
      B::integer(), 
      Base::integer().

start(A,B, Base) ->
    start(A, B, Base, {1}).

%% @doc TODO: add documentation
-spec start(A,B,Base, Options) -> ok when 
      A::integer(),
      B::integer(), 
      Base::integer(),
      Option::atom() | tuple(),
      Options::[Option].

start(A,B,Base, {Splits}) ->
   ListA = makeList(A),
   ListB = makeList(B),

   ASplit = split(ListA, Splits),
   BSplit = split(ListB, Splits),

   Segments = lists:seq(1,Splits),

   Workers = createWorkers(ASplit, BSplit, Base, Segments, self()),

   LastWorker = element(2,lists:keyfind(length(ASplit), 1, Workers)),
   LastWorker ! {length(ASplit)+1, 0},

   Sum = collect(length(ASplit), [], Workers),
   io:format("~n  ----------~n"),
    io:format("     ~w~n", [A]),
    io:format("     ~w~n", [B]),
    io:format(" + ---------~n"),
    io:format("    ~w~n",[lists:foldr(fun (ListA, ListB) -> ListB*10 + ListA end, 0, (lists:reverse(Sum)))]).

%% @doc divides the work among workers
-spec createWorkers(Alist, Blist, Base, Segments, PID) -> ok when
  Alist::[integer()],
  Blist::[integer()],
  Base::integer(),
  Segments::[integer()],
  PID::pid().

  createWorkers([],[],_,_,_) ->
  [];

  createWorkers([Ah|At], [Bh|Bt], Base, [Sh|St], PID) ->
  [{Sh, spawn_link(fun() -> worker(Ah,Bh,Base,Sh,PID) end)}
  | createWorkers(At,Bt,Base,St,PID)].


%% @doc
-spec worker(A,B,Base,S,PID) -> ok when
  A::[integer()],
  B::[integer()],
  Base::integer(),
  S::integer(),
  PID::pid().

  worker(A,B,Base,S,PID) ->
  SubworkerNoCarry = spawn_link(fun() -> subworker(A,B,Base,S,PID,0) end),
  SubworkerCarry = spawn_link(fun() -> subworker(A,B,Base,S,PID,1) end),

  receive
    {SegmentReceived,Carry} ->
    if (SegmentReceived -1 =/= S) ->
      exit(failSegementReceived);
      (Carry == 1) ->
      SubworkerCarry ! send,
      prntCarry(Carry),
      SubworkerNoCarry ! quit;
      true ->
      SubworkerNoCarry ! send,
      SubworkerCarry ! quit
    end
  end.

%% @doc 
-spec subworker(A,B,Base,S,PID,C) -> ok when
  A::[integer()],
  B::[integer()],
  Base::integer(),
  S::integer(),
  PID::pid(),
  C::integer().

  subworker(A,B,Base,S,PID,C) ->
  Result = addLists(A,B,Base,C),
  receive
    send ->
    PID ! {result, S, Result};
    quit ->
    ok
  end.

%% @doc 

collect(Segments, SumList, Workers) when 1 < length(Workers) -> 
    receive
  {result, SegmentNumber, NewSum} ->
  NewWorkers = lists:keydelete(SegmentNumber, 1, Workers),
       Worker = element(2,lists:keyfind(SegmentNumber-1, 1, NewWorkers)),
      Worker!{SegmentNumber,element(1,NewSum)},
      collect(Segments, [{SegmentNumber, NewSum}|SumList], NewWorkers)
    end;

collect(_Segments, SumList, _) ->
    receive
  {result, SegmentNumber, NewSum} ->
      SortedSumList = lists:keysort(1, [{SegmentNumber, NewSum} | SumList]),
      if element(1,NewSum) == 1 ->
        sumListsToSum([ {0,{0,1}} | SortedSumList], []);
         true ->
        sumListsToSum(SortedSumList, [])
      end
    end.


sumListsToSum([], Aux) ->
    lists:flatten(lists:reverse(Aux));

sumListsToSum([{_,{_,Sum}}|SortedSumList], Aux) ->
    sumListsToSum(SortedSumList,[Sum|Aux]).


%% @doc
-spec addLists(A,B,Base,C) -> ok when
  A::[integer()],
  B::[integer()],
  Base::integer(),
  C::integer().

  addLists(A,B,Base,C) ->
    Arev = lists:reverse(A),
    Brev = lists:reverse(B),
    addLists_aux(Arev,Brev,Base, [], C).

addLists_aux([],[],_,List,C) ->
    {C,List};


addLists_aux([LeastA|A], [LeastB|B], Base, List, C) ->
    {NewCarry, Result} = addSingular(LeastA+C, LeastB, Base),
      addLists_aux(A, B, Base, [Result|List], C).


%% @doc adds
addSingular(A,B, Base) ->
    Sum = A+B,
    SumRem = Sum rem Base,
    if (SumRem =/= Sum) ->
      {1, SumRem};
       true ->
      {0, Sum}
    end.

  %% @doc split the list L in to N segements of the same length
  -spec split(L,N) -> ok when
  L::[integer()],
  N::integer().

  split(L,N) -> 
  S = length(L) div N,
  R = length(L) rem N,
  splitaux(L,N,S,R,[]).

-spec splitaux(L1,N, S, R,L2) -> ok when
L1::[integer()],
N::integer(),
S::integer(),
R::integer(),
L2::[integer()].

splitaux([],_,_,_,ListOut) -> lists:reverse(ListOut);

splitaux(ListIn, N, S, 0, ListOut)->
    {List2, List3} = lists:split(S, ListIn),
    splitaux(List3,N,S,0,[List2|ListOut]);

splitaux(ListIn, N, S, R, ListOut) ->
NewL = fillaux(length(ListIn)+(N-R),ListIn),
NewS = length(NewL) div N,
splitaux(NewL, N, NewS, 0, ListOut).



%% @doc extends the shortest list with zeros.
-spec fill({A,B}) -> ok when
A::[integer()],
B::[integer()].

fill({A,B}) -> 
MaxL = max(length(A),length(B)),
{fillaux(MaxL,A),fillaux(MaxL,B)}.


%% Help function for fill, Fills the list L with zeros untill the length(L)=M
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


%% doc prints the int C 
  -spec prntCarry(C) -> ok when
  C::integer().

  prntCarry(C) ->
  if(C == 1) ->
  %     io:format(" "),
  io:format("~w", [C]);
  true ->
  io:format("0")
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

  split_test_() ->
  [?_assertEqual([[1,2],[3,4],[5,6]],split([1,2,3,4,5,6],3))],
  [?_assertEqual([[0,1],[2,4],[3,5],[7,6]],split([1,2,4,3,5,7,6],4))],
  [?_assertEqual([[0,3],[3,3]],split([3,3,3],2))].



%    Saker att skriva.
%    
%    Dela upp listor
%
%
%
%
% 1. Gör om till listor
% 2. Gör dom lika långa
% 3. Dela upp i N segment
% 4. skicka segmenten till N olika workers
%    4.1 
% 5. utför beräknigar
% 6. skriv ut svar