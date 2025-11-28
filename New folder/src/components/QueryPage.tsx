import { useState } from "react";
import { useQuery, useMutation } from "convex/react";
import { api } from "../../convex/_generated/api";

interface QueryPageProps {
  profile: any;
  user: any;
}

export default function QueryPage({ profile, user }: QueryPageProps) {
  const [question, setQuestion] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [replyTexts, setReplyTexts] = useState<Record<string, string>>({});

  const queries = useQuery(api.queries.list) || [];
  const createQuery = useMutation(api.queries.create);
  const answerQuery = useMutation(api.queries.answer);

  const handleSubmitQuestion = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!question.trim()) return;

    setIsSubmitting(true);
    try {
      await createQuery({ question: question.trim() });
      setQuestion("");
    } catch (error) {
      console.error("Failed to create query:", error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleReply = async (queryId: string) => {
    const replyText = replyTexts[queryId]?.trim();
    if (!replyText) return;

    try {
      await answerQuery({ queryId: queryId as any, answer: replyText });
      setReplyTexts({ ...replyTexts, [queryId]: "" });
    } catch (error) {
      console.error("Failed to answer query:", error);
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <h1 className="text-2xl font-bold text-gray-900">
        {profile?.role === "student" ? "💬 Ask Your Questions" : "💬 Student Queries"}
      </h1>

      {/* Question Form (Students Only) */}
      {profile?.role === "student" && (
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">
            Have a question? Ask your teachers!
          </h2>
          <form onSubmit={handleSubmitQuestion} className="space-y-4">
            <textarea
              value={question}
              onChange={(e) => setQuestion(e.target.value)}
              placeholder="What would you like to know?"
              rows={3}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
            />
            <div className="flex justify-end">
              <button
                type="submit"
                disabled={isSubmitting || !question.trim()}
                className="px-4 py-2 bg-purple-600 text-white rounded-md hover:bg-purple-700 disabled:opacity-50"
              >
                {isSubmitting ? "Asking..." : "❓ Ask Question"}
              </button>
            </div>
          </form>
        </div>
      )}

      {/* Queries List */}
      <div className="space-y-4">
        {queries.length === 0 ? (
          <div className="bg-white rounded-lg shadow p-8 text-center">
            <p className="text-gray-500">No questions asked yet.</p>
          </div>
        ) : (
          queries.map((query) => (
            <div key={query._id} className="bg-white rounded-lg shadow p-6">
              {/* Question */}
              <div className="mb-4">
                <div className="flex justify-between items-start mb-2">
                  <h3 className="font-semibold text-blue-600">
                    👤 {query.studentName}
                  </h3>
                  <span className="text-sm text-gray-500">
                    {new Date(query._creationTime).toLocaleDateString()} at{" "}
                    {new Date(query._creationTime).toLocaleTimeString()}
                  </span>
                </div>
                <p className="text-gray-800 whitespace-pre-wrap">{query.question}</p>
              </div>

              {/* Answer */}
              {query.answer ? (
                <div className="bg-green-50 border border-green-200 rounded-lg p-4">
                  <div className="flex justify-between items-start mb-2">
                    <h4 className="font-semibold text-green-600">
                      👨‍🏫 {query.teacherName} ⭐
                    </h4>
                    <span className="text-sm text-gray-500">
                      {query.answeredAt && new Date(query.answeredAt).toLocaleDateString()} at{" "}
                      {query.answeredAt && new Date(query.answeredAt).toLocaleTimeString()}
                    </span>
                  </div>
                  <p className="text-gray-800 whitespace-pre-wrap">{query.answer}</p>
                </div>
              ) : profile?.role === "teacher" ? (
                <div className="bg-red-50 border border-red-200 rounded-lg p-4">
                  <h4 className="font-semibold text-red-600 mb-2">💬 Reply to this question:</h4>
                  <div className="space-y-3">
                    <textarea
                      value={replyTexts[query._id] || ""}
                      onChange={(e) =>
                        setReplyTexts({ ...replyTexts, [query._id]: e.target.value })
                      }
                      placeholder="Write your answer..."
                      rows={2}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                    />
                    <div className="flex justify-end">
                      <button
                        onClick={() => handleReply(query._id)}
                        disabled={!replyTexts[query._id]?.trim()}
                        className="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 disabled:opacity-50"
                      >
                        📤 Reply
                      </button>
                    </div>
                  </div>
                </div>
              ) : (
                <div className="bg-gray-50 border border-gray-200 rounded-lg p-4">
                  <p className="text-gray-500 italic">⏳ Waiting for teacher's response...</p>
                </div>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
}
