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
    start(A, B, Base, [1]).

%% @doc TODO: add documentation
-spec start(A,B,Base, Options) -> ok when 
      A::integer(),
      B::integer(), 
      Base::integer(),
      Option::atom() | tuple(),
      Options::[Option].

start(A,B,Base, Options) ->
   ListA = makeList(A),
   ListB = makeList(B),

   ASplit = split(ListA, lists:nth(1, Options)),
   BSplit = split(ListB, lists:nth(1, Options)),
   {Acorr,Bcorr} = fill({ListA,ListB}),

   {Clist, Rlist} = dispatch(ASplit,BSplit,Base,self()),
  
   if length(Rlist) =:= length(Acorr) ->
    Roff = " ";
    true -> 
    Roff = ""
  end.



%% @doc Splits A and B into blocks of size Segsize and creates processes for each block that calculates the results.
-spec dispatch(A,B,Base,PID) -> ok when 
      A::list(),
      B::list(),
      Base::integer(),
      PID::pid().

dispatch([],[],_,PID) ->
    PID ! {[0],[]},
    receive
  {[C|Clist], Rlist} ->
      if C =:= 1 ->
        {[C|Clist],[49|Rlist]};
         C =:= 0 -> 
        {[C|Clist], Rlist}
      end
    end;
dispatch([Ah|At],[Bh|Bt],Base,PID) ->
    NewPid = spawn(add, adder, [Ah,Bh,Base,PID]),
    dispatch(At,Bt,Base,NewPid).


%% @doc Revereses the lists A and B and spawns two processes, one that counts with a carry bit and one that counts without a carry bit.
-spec adder(A,B,Base,PID) -> ok when 
      A::list(),
      B::list(),
      Base::integer(),
      PID::pid().
      
adder(A,B,Base,PID) ->
    Arev = lists:reverse(A),
    Brev = lists:reverse(B),
    Man0 = spawn(add,manager,[Arev,Brev,Base,0,PID]),
    Man1 = spawn(add,manager,[Arev,Brev,Base,1,PID]),
    receive
  {C, Rlist} ->
      Man0 ! {C,Rlist},  
      Man1 ! {C,Rlist}
    end.

%% @doc Checks if we have a carry bit or not and sends this information down to the child so that it can decide what to do.
-spec manager(A,B,Base,C,PID) -> ok when 
      A::list(),
      B::list(),
      C::list(),
      Base::integer(),
      PID::pid().

manager(A,B,Base,C,PID) ->
    Child = spawn(add,calculate,[A,B,Base,[C|[]],PID,[]]),
    receive
  {[CarryIn|Clist],Rlist}->
      if CarryIn=:= C ->
        Child ! {[CarryIn|Clist],Rlist};
         CarryIn =/= C->
        exit(Child,wrongCarry)
      end
    end.

%% @doc Calculates the result of A and B and stores it in Acc.
-spec calculate(A,B,Base,C,PID,Acc) -> ok when 
      A::list(),
      B::list(),
      C::list(),
      Base::integer(),
      PID::pid(),
      Acc::list().

calculate([],[],_,C,Pid,Acc) ->
    receive {Cin,Ain} ->
      {Cfix,_} = lists:split(length(C)-1,C),
      Aout = lists:append(Acc,Ain),
      Cout = lists:append(Cfix,Cin),
      Pid ! {Cout,Aout}
    end;
  

calculate([A|Alist],[B|Blist],Base,[C|Clist],PID,Acc) ->
    Adec = decode(A),
    Bdec = decode(B),
    Result = Adec + Bdec + C,
  
    {Cnew, Rnew} = encode(Result,Base),
    calculate(Alist,Blist,Base,[Cnew|[C|Clist]],PID,[Rnew|Acc]).


   
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

  %% @doc Converts the first character into a decimal number.
-spec decode([H|_]) -> ok when 
      H::integer().
decode(H) when H >=48, H =<57->
    H - 48;
decode(H) when H >=65, H =<90 ->
    H -55;
decode(H) ->
    io:fwrite("~w", [H]),
    0.



%% @doc Converts a decimal number into base Base.
-spec encode(Val,Base) -> ok when 
      Val::integer(),
      Base::integer().
encode(Val,Base) ->
    Q = Val div Base,
    Rem = Val rem Base,
    %%Rstr = io:format("~s",[[Rem]]),
    if Rem <10 ->
      Rstr = Rem+48;
       Rem >=10 -> 
      Rstr = Rem+55
    end,
    {Q,Rstr}.
    



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